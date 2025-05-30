package com.example.demo.controller

import com.example.demo.model.Product
import com.example.demo.model.User
import com.example.demo.repository.ProductRepository
import com.example.demo.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.Optional

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productRepository: ProductRepository

    @MockBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    // Dummy user and product for tests
    private val testUser = User(id = 1L, username = "testseller", email = "seller@example.com")
    private val anotherUser = User(id = 2L, username = "anotherseller", email = "another@example.com")
    private val testProduct = Product(id = 1L, name = "Test Product", price = 10.99, category = "Test Category", seller = testUser)
    private val testProductRequest = ProductRequest(name = "Test Product", price = 10.99, category = "Test Category", sellerId = 1L)

    @Test
    fun `createProduct should return CREATED when seller exists`() {
        whenever(userRepository.findById(testUser.id!!)).thenReturn(Optional.of(testUser))
        whenever(productRepository.save(any<Product>())).thenReturn(testProduct)

        mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testProductRequest)))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(testProduct.id))
            .andExpect(jsonPath("$.name").value(testProduct.name))
            .andExpect(jsonPath("$.price").value(testProduct.price))
            .andExpect(jsonPath("$.category").value(testProduct.category))
            .andExpect(jsonPath("$.seller.id").value(testUser.id))
            .andExpect(jsonPath("$.seller.username").value(testUser.username))
    }

    @Test
    fun `createProduct should return BAD_REQUEST when seller does not exist`() {
        whenever(userRepository.findById(any<Long>())).thenReturn(Optional.empty())

        mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testProductRequest)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `getProductById should return OK with product when product exists`() {
        whenever(productRepository.findById(testProduct.id!!)).thenReturn(Optional.of(testProduct))

        mockMvc.perform(get("/api/products/${testProduct.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(testProduct.id))
            .andExpect(jsonPath("$.name").value(testProduct.name))
            .andExpect(jsonPath("$.price").value(testProduct.price))
            .andExpect(jsonPath("$.category").value(testProduct.category))
            .andExpect(jsonPath("$.seller.id").value(testUser.id))
    }
    
    @Test
    fun `getProductById should return NOT_FOUND when product does not exist`() {
        whenever(productRepository.findById(any<Long>())).thenReturn(Optional.empty())

        mockMvc.perform(get("/api/products/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getAllProducts should return OK with list of products`() {
        val product2 = Product(id = 2L, name = "Another Product", price = 20.0, category = "Other Category", seller = anotherUser)
        val products = listOf(testProduct, product2)
        whenever(productRepository.findAll()).thenReturn(products)

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(products.size))
            .andExpect(jsonPath("$[0].id").value(testProduct.id))
            .andExpect(jsonPath("$[0].name").value(testProduct.name))
            .andExpect(jsonPath("$[1].id").value(product2.id))
            .andExpect(jsonPath("$[1].name").value(product2.name))
    }
    
    @Test
    fun `getAllProducts should return OK with empty list when no products exist`() {
        whenever(productRepository.findAll()).thenReturn(emptyList())

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(0))
    }
}
