package com.example.demo.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Client(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var firstName: String,
    var lastName: String,
    var email: String,
    var job: String = "no data",
    var position: String = "no data",
    var gender: String
)