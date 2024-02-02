package nl.gamma.shoppingcart.controller

import nl.gamma.shoppingcart.controller.model.AddProductRequest
import nl.gamma.shoppingcart.controller.model.GetProductResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProductController {
    @GetMapping("/api/product/{id}")
    fun getProduct(@PathVariable("id") productId: String): GetProductResponse {
        return GetProductResponse(productId, "Houten Tafel", "Deze tafel is perfect geschikt voor vier personen.", 5)
    }

    @PostMapping("/api/product/")
    fun addProduct(@RequestBody body: AddProductRequest): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/api/product/{id}")
    fun deleteProduct(@PathVariable("id") productId: String): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}
