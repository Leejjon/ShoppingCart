package nl.gamma.shoppingcart.scheduler

import nl.gamma.shoppingcart.businesslogic.interfaces.ReservationInterface
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CleanReservations(val reservationsInterface: ReservationInterface) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(cron = "0/5 * * * * ?")
    fun cleanupReservationsEveryFiveMinutes() {
        reservationsInterface.cleanUpExpiredReservations()
    }
}
