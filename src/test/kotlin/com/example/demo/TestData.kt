package com.example.demo

import com.example.demo.dto.ClientDto
import com.example.demo.dto.GenderizeResponse
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

fun getListOfClients() : List<Client> = listOf(
    Client(id = 1, firstName = "Mick", lastName = "Jagger", email = "m.jagger@test.com", job = "Singer", position = "Vocalist", gender = "male"),
    Client(id = 2, firstName = "Freddie", lastName = "Mercury", email = "f.mercury@test.com", job = "Singer", position = "Vocalist", gender = "male"),
    Client(id = 3, firstName = "David", lastName = "Bowie", email = "d.bowie@test.com", job = "Singer", position = "Vocalist", gender = "male"),
    Client(id = 4, firstName = "Axel", lastName = "Rose", email = "a.rose@test.com", job = "Singer", position = "Vocalist", gender = "male"),
    Client(id = 5, firstName = "James", lastName = "Hetfield", email = "j.hetfield@test.com", job = "Singer", position = "Vocalist", gender = "male")
)

fun getValidGenderizeResponse() : GenderizeResponse =
    GenderizeResponse("John", "male", 0.99f, 1)

fun getInvalidGenderizeResponse() : GenderizeResponse =
    GenderizeResponse("John", "male", 0.7f, 1)
