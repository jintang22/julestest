package com.example.demo.repository

import com.example.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository // Optional, Spring Data JPA repositories are automatically detected
interface UserRepository : JpaRepository<User, Long> {
    // You can add custom query methods here if needed later
    // For example: fun findByUsername(username: String): User?
}
