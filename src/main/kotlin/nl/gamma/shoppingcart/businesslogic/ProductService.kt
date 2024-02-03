package nl.gamma.shoppingcart.businesslogic

import nl.gamma.shoppingcart.businesslogic.interfaces.ProductsInterface
import nl.gamma.shoppingcart.businesslogic.model.ProductWithAvailability
import nl.gamma.shoppingcart.db.queryinterfaces.ProductsQueriesInterface
import org.springframework.stereotype.Service

@Service
class ProductService(val productsQueriesInterface: ProductsQueriesInterface): ProductsInterface {
    override fun getProductsWithAvailability(): List<ProductWithAvailability> {
        return productsQueriesInterface.getProducts().map { product -> ProductWithAvailability(product.id, product.name, product.color, product.description, product.totalSupplyInAllStores)}
    }

}
