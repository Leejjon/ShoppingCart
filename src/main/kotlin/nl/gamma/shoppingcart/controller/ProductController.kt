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

    @GetMapping("/products/{id}")
    fun getProduct(@PathVariable("id") productId: String): GetProductResponse {
        val productIdThatIsAnInt : Int
        try {
            productIdThatIsAnInt = productId.toInt()
        } catch (nfe: NumberFormatException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
        return GetProductResponse(productIdThatIsAnInt, "Houten Tafel", "geel", "Deze tafel is perfect geschikt voor vier personen.", 5)
    }
}
