package nl.gamma.shoppingcart.businesslogic.model

data class UpdateStockForProduct(val productId: Int, val storeId: Int, val quantityDelta: Int)
