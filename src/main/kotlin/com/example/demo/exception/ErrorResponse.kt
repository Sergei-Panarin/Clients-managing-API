package com.example.demo.exception

import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

class ErrorResponse (
    val timestamp: ZonedDateTime,
    val code: Int,
    val status: String,
    val error: String,
    val message: String
) {
}