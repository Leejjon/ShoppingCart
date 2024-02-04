package nl.gamma.shoppingcart.businesslogic

import nl.gamma.shoppingcart.businesslogic.interfaces.ReservationInterface
import nl.gamma.shoppingcart.businesslogic.model.Order
import nl.gamma.shoppingcart.db.queryinterfaces.StockQueriesInterface
import org.springframework.stereotype.Service

@Service
class ReservationService(val stockQueriesInterface: StockQueriesInterface) : ReservationInterface {
    override fun reserveProduct(productId: Int, quantity: Int, ipAddress: String): Number {
        return stockQueriesInterface.reserveProduct(productId, quantity, ipAddress)
    }

    override fun confirmReservation(reservationId: Int, ipAddress: String): Order {
        return stockQueriesInterface.confirmReservation(reservationId, ipAddress)
    }
}
