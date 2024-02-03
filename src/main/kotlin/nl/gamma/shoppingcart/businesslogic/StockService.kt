package nl.gamma.shoppingcart.businesslogic

import nl.gamma.shoppingcart.businesslogic.interfaces.StockInterface
import nl.gamma.shoppingcart.businesslogic.model.CreateStockForProduct
import nl.gamma.shoppingcart.businesslogic.model.Store
import nl.gamma.shoppingcart.businesslogic.model.StoreStockForProduct
import nl.gamma.shoppingcart.businesslogic.model.UpdateStockForProduct
import nl.gamma.shoppingcart.db.queryinterfaces.StockQueriesInterface
import org.springframework.stereotype.Service

@Service
class StockService(val stockQueriesInterface: StockQueriesInterface) : StockInterface {
    override fun updateStoreStockForProduct(updateStockForProduct: UpdateStockForProduct): Boolean {
        return stockQueriesInterface.updateStockForProduct(updateStockForProduct)
    }

    override fun deleteStoreStockForProduct(storeId: Int, productId: Int): Boolean {
        return stockQueriesInterface.deleteStoreStockForProduct(storeId, productId)
    }

    override fun getStores(): List<Store> {
        return stockQueriesInterface.getStores()
    }

    override fun getStoreStockForProduct(storeId: Int, productId: Int): StoreStockForProduct {
        return stockQueriesInterface.getStoreStockForProduct(storeId, productId)
    }

    override fun createStoreStockForProduct(createStockForProduct: CreateStockForProduct): Boolean {
        return stockQueriesInterface.addStockForProduct(createStockForProduct)
    }
}
