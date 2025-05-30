package com.example.demo.controller

import com.example.demo.model.Product
import com.example.demo.model.User
import com.example.demo.repository.ProductRepository
import com.example.demo.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.* // Required for Optional

// Data class for the request body when creating a product
data class ProductRequest(
    val name: String,
    val price: Double,
    val category: String,
    val sellerId: Long
)

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository // To fetch seller information
) {

    @PostMapping
    fun createProduct(@RequestBody productRequest: ProductRequest): ResponseEntity<Product> {
        val sellerOptional: Optional<User> = userRepository.findById(productRequest.sellerId)
        
        if (!sellerOptional.isPresent) {
            return ResponseEntity.badRequest().build() // Or ResponseEntity.notFound().build()
        }
        
        val seller: User = sellerOptional.get()
        
        val product = Product(
            name = productRequest.name,
            price = productRequest.price,
            category = productRequest.category,
            seller = seller
        )
        
        val savedProduct = productRepository.save(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        return productRepository.findById(id)
            .map { product -> ResponseEntity.ok(product) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping
    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }
}
