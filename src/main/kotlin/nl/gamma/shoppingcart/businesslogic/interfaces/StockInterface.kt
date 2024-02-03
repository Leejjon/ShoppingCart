package nl.gamma.shoppingcart.businesslogic.interfaces

import nl.gamma.shoppingcart.businesslogic.model.CreateStockForProduct
import nl.gamma.shoppingcart.businesslogic.model.Store
import nl.gamma.shoppingcart.businesslogic.model.StoreStockForProduct
import nl.gamma.shoppingcart.businesslogic.model.UpdateStockForProduct

interface StockInterface {
    fun getStores(): List<Store>
    fun getStoreStockForProduct(storeId: Int, productId: Int): StoreStockForProduct
    fun createStoreStockForProduct(createStockForProduct: CreateStockForProduct): Boolean
    fun updateStoreStockForProduct(updateStockForProduct: UpdateStockForProduct): Boolean
    fun deleteStoreStockForProduct(storeId: Int, productId: Int): Boolean
}
