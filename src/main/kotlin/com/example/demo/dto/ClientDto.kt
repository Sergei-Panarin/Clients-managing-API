package com.example.demo.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ClientDto(
    var id: Long? = null,

    @field:NotBlank(message = "FirstName can not be blank")
    var firstName: String,

    @field:NotBlank(message = "LastName can not be blank")
    var lastName: String,

    @field:Email(message = "Invalid email")
    var email: String,

    var job: String = "no data",

    var position: String = "no data",

    var gender: String = "no data"
)