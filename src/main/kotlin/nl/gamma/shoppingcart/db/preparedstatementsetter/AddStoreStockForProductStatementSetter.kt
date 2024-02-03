package nl.gamma.shoppingcart.db.preparedstatementsetter

import nl.gamma.shoppingcart.businesslogic.model.CreateStockForProduct
import org.springframework.jdbc.core.PreparedStatementSetter
import java.sql.PreparedStatement

class AddStoreStockForProductStatementSetter(val createStockForProduct: CreateStockForProduct) : PreparedStatementSetter {
    override fun setValues(ps: PreparedStatement) {
        ps.setInt(1, createStockForProduct.initialQuantity)
        ps.setInt(2, createStockForProduct.productId)
        ps.setInt(3, createStockForProduct.storeId)
    }
}
