package com.example.demo.repository

import com.example.demo.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository // Optional, but good for consistency
interface ProductRepository : JpaRepository<Product, Long> {
    // Custom query methods can be added here later if needed
}
