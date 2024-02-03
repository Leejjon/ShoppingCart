package nl.gamma.shoppingcart.businesslogic.interfaces

import nl.gamma.shoppingcart.businesslogic.model.ProductWithAvailability

interface ProductsInterface {
    fun getProductsWithAvailability(): List<ProductWithAvailability>
}
