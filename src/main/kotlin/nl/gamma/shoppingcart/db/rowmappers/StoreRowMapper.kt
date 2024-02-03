package nl.gamma.shoppingcart.db.rowmappers

import nl.gamma.shoppingcart.businesslogic.model.Store
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class StoreRowMapper : RowMapper<Store> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Store {
        return Store(rs.getInt("id"), rs.getString("brand"), rs.getString("location"))
    }
}
