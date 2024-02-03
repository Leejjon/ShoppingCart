package nl.gamma.shoppingcart.db.rowmappers

import nl.gamma.shoppingcart.businesslogic.model.StoreStockForProduct
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class GetStoreStockForProductRowMapper : RowMapper<StoreStockForProduct> {
    override fun mapRow(rs: ResultSet, rowNum: Int): StoreStockForProduct? {
        return StoreStockForProduct(
            rs.getInt("STOREID"),
            rs.getString("BRAND"),
            rs.getString("LOCATION"),
            rs.getInt("PRODUCTID"),
            rs.getInt("QUANTITY")
        )
    }
}
