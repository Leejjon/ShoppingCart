package nl.gamma.shoppingcart.db.preparedstatementsetter

import nl.gamma.shoppingcart.businesslogic.model.UpdateStockForProduct
import org.springframework.jdbc.core.PreparedStatementSetter
import java.sql.PreparedStatement

class UpdateStockStatementSetter(val updateStockForProduct: UpdateStockForProduct) : PreparedStatementSetter {
    override fun setValues(ps: PreparedStatement) {
        ps.setInt(1, updateStockForProduct.quantityDelta)
        ps.setInt(2, updateStockForProduct.productId)
        ps.setInt(3, updateStockForProduct.storeId)
        ps.setInt(4, updateStockForProduct.quantityDelta)
    }
}
