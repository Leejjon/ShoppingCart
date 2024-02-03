package nl.gamma.shoppingcart.controller.model

data class GetStoreStockForProduct(val storeId: Int, val brand: String, val location: String, val productId: Int, val quantity: Int)
