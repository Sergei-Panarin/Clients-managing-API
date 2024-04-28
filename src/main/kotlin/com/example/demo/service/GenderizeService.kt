package com.example.demo.service

import com.example.demo.client.GenderizeClient
import com.example.demo.model.GenderizeResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GenderizeService(private val genderizeClient: GenderizeClient) {

    private val log = LoggerFactory.getLogger(GenderizeService::class.java)

    fun getGender(firstName: String): GenderizeResponse? {
        log.info("Sending request to Genderize API for name $firstName")

        return try {
            val response = genderizeClient.getGender(firstName)
            log.info("Received response from Genderize API for name $firstName: $response")
            response
        } catch (e: Exception) {
            log.error("Error while sending request to Genderize API for name $firstName", e)
            null
        }
    }
}