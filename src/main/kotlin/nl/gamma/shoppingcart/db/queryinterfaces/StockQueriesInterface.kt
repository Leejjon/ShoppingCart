package nl.gamma.shoppingcart.db.queryinterfaces

import nl.gamma.shoppingcart.businesslogic.model.CreateStockForProduct
import nl.gamma.shoppingcart.businesslogic.model.Store
import nl.gamma.shoppingcart.businesslogic.model.StoreStockForProduct
import nl.gamma.shoppingcart.businesslogic.model.UpdateStockForProduct

interface StockQueriesInterface {
    fun addStockForProduct(createStockForProduct: CreateStockForProduct): Boolean
    fun updateStockForProduct(updateStockForProduct: UpdateStockForProduct): Boolean
    fun deleteStoreStockForProduct(storeId: Int, productId: Int): Boolean
    fun getStores(): List<Store>
    fun getStoreStockForProduct(storeId: Int, productId: Int): StoreStockForProduct
}
