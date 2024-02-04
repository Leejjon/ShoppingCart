package nl.gamma.shoppingcart.controller

import nl.gamma.shoppingcart.businesslogic.interfaces.StockInterface
import nl.gamma.shoppingcart.businesslogic.model.CreateStockForProduct
import nl.gamma.shoppingcart.businesslogic.model.UpdateStockForProduct
import nl.gamma.shoppingcart.controller.model.CreateStockRequestBody
import nl.gamma.shoppingcart.controller.model.GetStoreStockForProduct
import nl.gamma.shoppingcart.controller.model.StoreResponse
import nl.gamma.shoppingcart.controller.model.UpdateStockRequestBody
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api")
class StockController(val stockInterface: StockInterface) {
    @GetMapping("/stores")
    fun getStores(): List<StoreResponse> {
        return stockInterface.getStores().map { store -> StoreResponse(store.id, store.brand, store.location) }
    }

    @GetMapping("/stores/{id}/product/{productId}/stock")
    fun getStoreStock(@PathVariable("storeId") storeId: Int,
                      @PathVariable("productId") productId: Int): GetStoreStockForProduct {
        val storeStockForProduct = stockInterface.getStoreStockForProduct(storeId, productId)
        return GetStoreStockForProduct(storeStockForProduct.storeId, storeStockForProduct.brand,
            storeStockForProduct.location, storeStockForProduct.productId, storeStockForProduct.quantity)
    }

    @PostMapping("/stores/{storeId}/product/{productId}/stock")
    fun createStockForProduct(@PathVariable("storeId") storeId: Int,
                              @PathVariable("productId") productId: Int,
                              @RequestBody requestBody: CreateStockRequestBody
    ): ResponseEntity<Unit> {
        val succesfull = stockInterface.createStoreStockForProduct(
            CreateStockForProduct(
                productId,
                storeId,
                requestBody.initialQuantity
            )
        )
        if (succesfull) {
            return ResponseEntity.ok().build()
        } else {
            // Should probably be a custom message with "already exists" or something.
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/stores/{storeId}/product/{productId}/stock")
    fun updateStockForProduct(@PathVariable("storeId") storeId: Int,
                              @PathVariable("productId") productId: Int,
                              @RequestBody requestBody: UpdateStockRequestBody): ResponseEntity<Unit> {
        val successful = stockInterface.updateStoreStockForProduct(
            UpdateStockForProduct(
                productId,
                storeId,
                requestBody.delta
            )
        )
        if (successful) {
            return ResponseEntity.ok().build()
        } else {
            // Should probably be a custom message with "stock can't be below zero" or something.
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/stores/{storeId}/product/{productId}/stock")
    fun deleteStoreStockForProduct(@PathVariable("storeId") storeId: Int,
                                   @PathVariable("productId") productId: Int): ResponseEntity<Unit>  {
        val successfull = stockInterface.deleteStoreStockForProduct(storeId, productId)
        if (successfull) {
            return ResponseEntity.ok().build()
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }
}
