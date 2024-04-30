package com.example.demo.service.impl

import com.example.demo.client.GenderizeClient
import com.example.demo.dto.GenderizeResponse
import com.example.demo.service.GenderizeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GenderizeServiceImpl(private val genderizeClient: GenderizeClient) : GenderizeService {

    private val log = LoggerFactory.getLogger(GenderizeServiceImpl::class.java)

    override fun getGender(firstName: String): GenderizeResponse? {
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