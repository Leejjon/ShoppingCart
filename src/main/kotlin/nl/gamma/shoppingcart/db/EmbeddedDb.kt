package nl.gamma.shoppingcart.db

import nl.gamma.shoppingcart.businesslogic.model.*
import nl.gamma.shoppingcart.db.model.Reservation
import nl.gamma.shoppingcart.db.model.StoreAndQuantity
import nl.gamma.shoppingcart.db.preparedstatementsetter.AddStoreStockForProductStatementSetter
import nl.gamma.shoppingcart.db.preparedstatementsetter.DeleteStoreStockStatementSetter
import nl.gamma.shoppingcart.db.preparedstatementsetter.GetStoreStockForProductStatementSetter
import nl.gamma.shoppingcart.db.preparedstatementsetter.UpdateStockStatementSetter
import nl.gamma.shoppingcart.db.queryinterfaces.ProductsQueriesInterface
import nl.gamma.shoppingcart.db.queryinterfaces.StockQueriesInterface
import nl.gamma.shoppingcart.db.rowmappers.GetStoreStockForProductRowMapper
import nl.gamma.shoppingcart.db.rowmappers.ProductAndQuantityRowMapper
import nl.gamma.shoppingcart.db.rowmappers.StoreRowMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.sql.*
import java.time.Instant


@Component
class EmbeddedDb(val jdbcTemplate: JdbcTemplate) : ProductsQueriesInterface, StockQueriesInterface {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun getProducts(): List<ProductWithAvailability> {
        return jdbcTemplate.query("SELECT SUM(QUANTITY) as STOCK, PRODUCT.ID, PRODUCT.NAME, PRODUCT.COLOR, PRODUCT.DESCRIPTION " +
                "FROM PRODUCT INNER JOIN STOCK S on PRODUCT.ID = S.PRODUCTID GROUP BY PRODUCT.ID",
            ProductAndQuantityRowMapper())
    }

    override fun getStores(): List<Store> {
        return jdbcTemplate.query("SELECT * FROM STORE", StoreRowMapper())
    }

    override fun addStockForProduct(createStockForProduct: CreateStockForProduct): Boolean {
        val update = jdbcTemplate.update("INSERT INTO STOCK (quantity, productid, storeid) VALUES (?, ?, ?)",
            AddStoreStockForProductStatementSetter(createStockForProduct))
        return update == 1 // This means rows have been affected
    }

    override fun updateStockForProduct(updateStockForProduct: UpdateStockForProduct): Boolean {
        val update = jdbcTemplate.update(
            "UPDATE STOCK STK SET STK.QUANTITY = STK.QUANTITY + ? WHERE STK.productId = ? AND STK.storeId = ? AND STK.QUANTITY + ? >= 0",
            UpdateStockStatementSetter(updateStockForProduct)
        )
        return update == 1 // This means rows have been affected
    }

    override fun deleteStoreStockForProduct(storeId: Int, productId: Int): Boolean {
        val update = jdbcTemplate.update("DELETE FROM STOCK WHERE STOREID = ? AND PRODUCTID = ?",
            DeleteStoreStockStatementSetter(storeId, productId))
        return update == 1
    }

    override fun getStoreStockForProduct(storeId: Int, productId: Int): StoreStockForProduct {
        val result = jdbcTemplate.query(
            "SELECT store.ID as storeId, brand, location, quantity, stock.PRODUCTID FROM STORE INNER JOIN STOCK ON STORE.ID = STOCK.storeId WHERE STORE.ID = ? AND STOCK.productId = ?",
            GetStoreStockForProductStatementSetter(storeId, productId),
            GetStoreStockForProductRowMapper()
        )
        if (result.size > 0) {
            return result.first()
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    @Transactional
    override fun reserveProduct(productId: Int, quantityToReserve: Int, ipAddress: String): Number {
        // Quantity has to be a positive number
        val result = jdbcTemplate.query(
            "SELECT quantity, storeId FROM STOCK WHERE productId = ? AND quantity >= ?",
            PreparedStatementSetter(fun(ps: PreparedStatement) {
                ps.setInt(1, productId)
                ps.setInt(2, quantityToReserve)
            }),
            fun(rs: ResultSet, rowNum: Int): StoreAndQuantity {
                return StoreAndQuantity(rs.getInt("storeId"), rs.getInt("quantity"))
            }
        )

        if (result.size > 0) {
            val storeStock = result.first()

            // Create the reservation
            val keyHolder: KeyHolder = GeneratedKeyHolder()
            jdbcTemplate.update({ connection: Connection ->
                val ps: PreparedStatement = connection.prepareStatement(
                    "INSERT INTO RESERVATIONS (expirationDate, ipaddress, productId, quantity, storeId) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                )
                val nowPlusFiveMinutes = Instant.now().plusSeconds(60 * 5)
                ps.setTimestamp(1, Timestamp.from(nowPlusFiveMinutes))
                ps.setString(2, ipAddress)
                ps.setInt(3, productId)
                ps.setInt(4, quantityToReserve)
                ps.setInt(5, storeStock.storeId)
                ps
            }, keyHolder)


            // Subtract the reserved quantity from the stock record for the store
            jdbcTemplate.update("UPDATE STOCK STK SET STK.QUANTITY = ? WHERE STOREID = ? AND PRODUCTID = ?",
                    fun(ps: PreparedStatement) {
                        ps.setInt(1, storeStock.quantity - quantityToReserve)
                        ps.setInt(2, storeStock.storeId)
                        ps.setInt(3, productId)
                    }
                )

            val reservationNumber = keyHolder.key
            if (reservationNumber != null) {
                log.info("Print generated primary key: " + reservationNumber)
                return reservationNumber
            } else {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            }
        } else { // TODO: If two different stores both have enough stock together for this order we could build a solution
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    @Transactional
    override fun confirmReservation(reservationId: Int, ipAddress: String): Order {
        val reservations = jdbcTemplate.query("SELECT id, productId, quantity, storeId FROM RESERVATIONS WHERE ipAddress = ?",
            PreparedStatementSetter { ps -> ps.setString(1, ipAddress) },
            RowMapper<Reservation> { rs, rowNum ->
                Reservation(
                    rs.getInt("id"),
                    rs.getInt("productId"),
                    rs.getInt("quantity"),
                    rs.getInt("storeId")
                )
            }
        )

        if (reservations.size == 1) {
            val reservation = reservations.first()
            // Delete the reservation
            jdbcTemplate.update("DELETE FROM RESERVATIONS WHERE ID = ?") { ps -> ps.setInt(1, reservationId) }

            // Create the order
            val keyHolder: KeyHolder = GeneratedKeyHolder()
            jdbcTemplate.update({ connection: Connection ->
                val ps: PreparedStatement = connection.prepareStatement(
                    "INSERT INTO ORDERS (ipaddress, productId, quantity, storeId) VALUES(?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                )
                ps.setString(1, ipAddress)
                ps.setInt(2, reservation.productId)
                ps.setInt(3, reservation.quantity)
                ps.setInt(4, reservation.storeId)
                ps
            }, keyHolder)

            val stores = jdbcTemplate.query("SELECT brand, location FROM STORE where id = ?",
                PreparedStatementSetter { ps -> ps.setInt(1, reservation.storeId) },
                RowMapper<nl.gamma.shoppingcart.db.model.Store> { rs, rowNum ->
                    nl.gamma.shoppingcart.db.model.Store(
                        rs.getString(
                            "brand"
                        ), rs.getString("location")
                    )
                }
            )

            val key = keyHolder.key
            if (key != null) {
                val store = stores.first()
                return Order(key, ipAddress, reservation.productId, reservation.quantity, store.brand, store.location)
            } else {
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }
}
