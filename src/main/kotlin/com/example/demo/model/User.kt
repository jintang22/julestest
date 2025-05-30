package com.example.demo.model

import jakarta.persistence.* // Ensure all necessary JPA imports are here

@Entity
@Table(name = "users") // It's good practice to explicitly name the table, e.g., "users"
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var username: String,

    var email: String,

    @OneToMany(mappedBy = "seller", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val products: List<Product> = emptyList()
)
