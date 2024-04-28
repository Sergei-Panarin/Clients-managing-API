package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GenderizeResponse(
    val name: String,
    val gender: String,
    val probability: Float,
    val count: Int
)