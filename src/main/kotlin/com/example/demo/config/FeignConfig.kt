package com.example.demo.config

import feign.Request
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class FeignConfig {

    @Bean
    fun options(): Request.Options {
        return Request.Options(3, TimeUnit.SECONDS, 3, TimeUnit.SECONDS, true)
    }
}