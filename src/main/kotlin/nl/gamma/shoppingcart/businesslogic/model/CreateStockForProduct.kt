package nl.gamma.shoppingcart.businesslogic.model

data class CreateStockForProduct(val productId: Int, val storeId: Int, val initialQuantity: Int)
