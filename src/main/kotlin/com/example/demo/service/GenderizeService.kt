package com.example.demo.service

import com.example.demo.dto.GenderizeResponse

interface GenderizeService {
    fun getGender(firstName: String): GenderizeResponse?
}