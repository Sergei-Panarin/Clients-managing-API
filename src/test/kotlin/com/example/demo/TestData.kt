package com.example.demo

import com.example.demo.dto.ClientDto
import com.example.demo.model.Client

fun getClientDto() : ClientDto = ClientDto(
        id = 1L,
        firstName = "John",
        lastName = "Doe",
        email = "j.doe@test.com",
        job = "Andersen",
        position = "Senior Developer",
        gender = "male"
    )

fun getClient() : Client = Client(
    id = 1L,
    firstName = "John",
    lastName = "Doe",
    email = "j.doe@test.com",
    job = "Andersen",
    position = "Senior Developer",
    gender = "male"
)
