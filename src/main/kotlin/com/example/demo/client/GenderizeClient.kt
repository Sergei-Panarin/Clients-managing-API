package com.example.demo.client

import com.example.demo.config.FeignConfig
import com.example.demo.dto.GenderizeResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "genderize", url = "\${genderize.api-url}", configuration = [FeignConfig::class])
interface GenderizeClient {

    @GetMapping("/")
    fun getGender(@RequestParam("name") firstName: String): GenderizeResponse
}