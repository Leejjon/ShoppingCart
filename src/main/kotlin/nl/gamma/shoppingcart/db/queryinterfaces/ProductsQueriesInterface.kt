package nl.gamma.shoppingcart.db.queryinterfaces

import nl.gamma.shoppingcart.businesslogic.model.ProductWithAvailability

interface ProductsQueriesInterface {
    fun getProducts(): List<ProductWithAvailability>
}
