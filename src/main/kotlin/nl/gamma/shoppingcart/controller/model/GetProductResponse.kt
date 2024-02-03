package nl.gamma.shoppingcart.controller.model

data class GetProductResponse(val id: Int, val name: String, val color: String, val description: String, val totalSupplyInAllStores: Int)
