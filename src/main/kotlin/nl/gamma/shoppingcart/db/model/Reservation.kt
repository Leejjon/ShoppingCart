package nl.gamma.shoppingcart.db.model

data class Reservation(val reservationId: Int, val productId: Int, val quantity: Int, val storeId: Int)
