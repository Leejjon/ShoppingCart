package nl.gamma.shoppingcart.controller.model

data class GetProductResponse(val id: String, val name: String, val description: String, val totalSupplyInAllStores: Int)
