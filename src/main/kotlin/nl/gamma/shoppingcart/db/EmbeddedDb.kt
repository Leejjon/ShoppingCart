package nl.gamma.shoppingcart.db

import nl.gamma.shoppingcart.businesslogic.model.*
import nl.gamma.shoppingcart.db.preparedstatementsetter.AddStoreStockForProductStatementSetter
import nl.gamma.shoppingcart.db.preparedstatementsetter.DeleteStoreStockStatementSetter
import nl.gamma.shoppingcart.db.rowmappers.ProductAndQuantityRowMapper
import nl.gamma.shoppingcart.db.rowmappers.StoreRowMapper
import nl.gamma.shoppingcart.db.preparedstatementsetter.GetStoreStockForProductStatementSetter
import nl.gamma.shoppingcart.db.preparedstatementsetter.UpdateStockStatementSetter
import nl.gamma.shoppingcart.db.queryinterfaces.ProductsQueriesInterface
import nl.gamma.shoppingcart.db.queryinterfaces.StockQueriesInterface
import nl.gamma.shoppingcart.db.rowmappers.GetStoreStockForProductRowMapper
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

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
        return jdbcTemplate.query("SELECT store.ID as storeId, brand, location, quantity, stock.PRODUCTID FROM STORE INNER JOIN STOCK ON STORE.ID = STOCK.storeId WHERE STORE.ID = ? AND STOCK.productId = ?",
            GetStoreStockForProductStatementSetter(storeId, productId),
            GetStoreStockForProductRowMapper()).first()
        // TODO: Handle it nicely if it's not there.
    }
}
