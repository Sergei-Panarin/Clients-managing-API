package com.example.demo.exception

class ServiceException(
    val error: ServiceError,
    override val message: String
) : RuntimeException(message) {
}