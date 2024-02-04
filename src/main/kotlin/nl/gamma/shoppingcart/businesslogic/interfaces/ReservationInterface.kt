package nl.gamma.shoppingcart.businesslogic.interfaces

import nl.gamma.shoppingcart.businesslogic.model.Order

interface ReservationInterface {
    fun reserveProduct(productId: Int, quantity: Int, ipAddress: String): Number
    fun confirmReservation(reservationId: Int, ipAddress: String): Order
    fun cleanUpExpiredReservations()
}
