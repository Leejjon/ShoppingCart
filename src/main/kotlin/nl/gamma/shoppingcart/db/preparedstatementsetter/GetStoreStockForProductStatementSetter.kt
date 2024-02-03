package nl.gamma.shoppingcart.db.preparedstatementsetter

import org.springframework.jdbc.core.PreparedStatementSetter
import java.sql.PreparedStatement

class GetStoreStockForProductStatementSetter(val storeId: Int, val productId: Int) : PreparedStatementSetter {
    override fun setValues(ps: PreparedStatement) {
        ps.setInt(1, storeId)
        ps.setInt(2, productId)
    }
}
