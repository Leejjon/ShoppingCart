package nl.gamma.shoppingcart.businesslogic.model

data class Order(val orderId: Number, val ipAddress: String, val productId: Int, val quantity: Int, val brand: String, val location: String)
