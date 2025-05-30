package com.example.demo.model

import jakarta.persistence.*

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String,
    var price: Double,
    var category: String,

    @ManyToOne(fetch = FetchType.LAZY) // LAZY fetching is generally preferred for performance
    @JoinColumn(name = "seller_id") // This explicitly defines the foreign key column
    var seller: User
)
