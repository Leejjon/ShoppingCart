package nl.gamma.shoppingcart.controller

import nl.gamma.shoppingcart.businesslogic.interfaces.ProductsInterface
import nl.gamma.shoppingcart.controller.model.AddProductRequest
import nl.gamma.shoppingcart.controller.model.GetProductResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api")
class ProductController(val productsInterface: ProductsInterface) {
    @GetMapping("/products")
    fun getProducts(): List<GetProductResponse> {
        return productsInterface.getProductsWithAvailability().map { product -> GetProductResponse(product.id, product.name, product.color, product.description, product.totalSupplyInAllStores) }
    }
}
