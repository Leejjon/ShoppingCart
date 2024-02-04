package nl.gamma.shoppingcart.db.queryinterfaces

import nl.gamma.shoppingcart.businesslogic.model.*

interface StockQueriesInterface {
    fun addStockForProduct(createStockForProduct: CreateStockForProduct): Boolean
    fun updateStockForProduct(updateStockForProduct: UpdateStockForProduct): Boolean
    fun deleteStoreStockForProduct(storeId: Int, productId: Int): Boolean
    fun getStores(): List<Store>
    fun getStoreStockForProduct(storeId: Int, productId: Int): StoreStockForProduct
    fun reserveProduct(productId: Int, quantityToReserve: Int, ipAddress: String): Number
    fun confirmReservation(reservationId: Int, ipAddress: String): Order
}
