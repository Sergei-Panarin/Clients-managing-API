package com.example.demo.exception

import org.slf4j.LoggerFactory
import org.springframework.beans.TypeMismatchException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.AccessDeniedException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.ZonedDateTime

@RestControllerAdvice
class ExceptionControllerHandler {

    private val log = LoggerFactory.getLogger(ExceptionControllerHandler::class.java)

    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(ex: ServiceException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("ServiceException occurred: ", ex)
        val error = ex.error
        val errorResponse = ErrorResponse(
            timestamp = ZonedDateTime.now(),
            code = error.status.value(),
            status = error.status.name,
            error = ex.javaClass.simpleName,
            message = ex.message ?: error.message
        )
        return ResponseEntity(errorResponse, error.status)
    }

    @ExceptionHandler(TypeMismatchException::class)
    fun handleTypeMismatchException(ex: TypeMismatchException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("TypeMismatchException occurred: ", ex)
        val error = ServiceError.BAD_REQUEST
        val errorResponse = ErrorResponse(
            timestamp = ZonedDateTime.now(),
            code = error.status.value(),
            status = error.status.name,
            error = ex.javaClass.simpleName,
            message = ex.message ?: error.message
        )
        return ResponseEntity(errorResponse, error.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("MethodArgumentNotValidException occurred: ", ex)
        val error = ServiceError.BAD_REQUEST
        val errorResponse = ErrorResponse(
            timestamp = ZonedDateTime.now(),
            code = error.status.value(),
            status = error.status.name,
            error = ex.javaClass.simpleName,
            message = ex.message ?: error.message
        )
        return ResponseEntity(errorResponse, error.status)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("DataIntegrityViolationException occurred: ", ex)
        val error = ServiceError.BAD_REQUEST
        val errorResponse = ErrorResponse(
            timestamp = ZonedDateTime.now(),
            code = error.status.value(),
            status = error.status.name,
            error = ex.javaClass.simpleName,
            message = ex.message ?: error.message
        )
        return ResponseEntity(errorResponse, error.status)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("AccessDeniedException occurred: ", ex)
        val error = ServiceError.FORBIDDEN
        val errorResponse = ErrorResponse(
            timestamp = ZonedDateTime.now(),
            code = error.status.value(),
            status = error.status.name,
            error = ex.javaClass.simpleName,
            message = ex.message ?: error.message
        )
        return ResponseEntity(errorResponse, error.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        log.error("Exception occurred: ", ex)
        val error = ServiceError.INTERNAL_SERVER_ERROR
        val errorResponse = ErrorResponse(
            timestamp = ZonedDateTime.now(),
            code = error.status.value(),
            status = error.status.name,
            error = ex.javaClass.simpleName,
            message = ex.message ?: error.message
        )
        return ResponseEntity(errorResponse, error.status)
    }
}