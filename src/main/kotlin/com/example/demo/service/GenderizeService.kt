package com.example.demo.service

import com.example.demo.model.GenderizeResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class GenderizeService {
    private val client = OkHttpClient.Builder()
        .callTimeout(3, TimeUnit.SECONDS)
        .build()
    private val mapper = jacksonObjectMapper()
    private val log = LoggerFactory.getLogger(GenderizeService::class.java)

    fun getGender(firstName: String): GenderizeResponse? {

        val request = Request.Builder()
            .url("https://api.genderize.io/?name=$firstName") //TODO*****************************************************
            .build()
        log.info("Sending request to Genderize API for name $firstName")

        return try {
        client.newCall(request).execute().use { response ->
            val body = response.body.string()
            run {
                log.info("Received response from Genderize API for name $firstName: $body")
                return mapper.readValue<GenderizeResponse>(body)
            }
        }
        } catch (e: Exception) {
            log.error("Error while sending request to Genderize API for name $firstName", e)
            null
        }
    }
}