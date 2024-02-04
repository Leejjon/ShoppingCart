package nl.gamma.shoppingcart.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import nl.gamma.shoppingcart.businesslogic.interfaces.ReservationInterface
import nl.gamma.shoppingcart.businesslogic.model.Order
import nl.gamma.shoppingcart.controller.model.ReservationRequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ReservationController(val reservationInterface: ReservationInterface) {
    @PostMapping("/reservations/")
    fun reserveQuantityOfProduct(@Valid @RequestBody reservationRequestBody: ReservationRequestBody, request: HttpServletRequest): Number {
        return reservationInterface.reserveProduct(reservationRequestBody.productId, reservationRequestBody.quantity, request.remoteAddr)
    }

    @PostMapping("/reservations/order/{id}")
    fun confirmReservation(@PathVariable("id") reservationId: Int, request: HttpServletRequest): Order {
        return reservationInterface.confirmReservation(reservationId, request.remoteAddr)
    }
}
