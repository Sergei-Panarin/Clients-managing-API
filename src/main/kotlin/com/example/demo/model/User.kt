package com.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(unique = true, nullable = false)
    val username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var roles: String
)