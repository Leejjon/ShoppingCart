package nl.gamma.shoppingcart.controller.model

import jakarta.validation.constraints.Min

data class ReservationRequestBody(
    val productId: Int,
    @Min(value = 0, message = "We don't allow negative quantities")
    val quantity: Int
)
