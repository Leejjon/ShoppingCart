package nl.gamma.shoppingcart.db.rowmappers

import nl.gamma.shoppingcart.businesslogic.model.ProductWithAvailability
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ProductAndQuantityRowMapper : RowMapper<ProductWithAvailability> {
    override fun mapRow(rs: ResultSet, rowNum: Int): ProductWithAvailability {
        return ProductWithAvailability(rs.getInt("ID"), rs.getString("NAME"),
            rs.getString("COLOR"), rs.getString("DESCRIPTION"), rs.getInt("STOCK"))
    }
}
