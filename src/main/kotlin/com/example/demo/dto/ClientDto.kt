package com.example.demo.dto

data class ClientDto(
    var id: Long? = null,
    var firstName: String,
    var lastName: String,
    var email: String,
    var job: String = "no data",
    var position: String = "no data",
    var gender: String = "no data"
)