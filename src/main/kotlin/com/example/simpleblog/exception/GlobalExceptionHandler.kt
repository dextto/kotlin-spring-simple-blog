package com.example.simpleblog.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    val log = KotlinLogging.logger { }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.error { "handleMethodArgumentNotValidException $e" }

        return ResponseEntity(
            ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult),
            HttpStatus.BAD_REQUEST,
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        log.error { "handleEntityNotFoundException $e" }

        return ResponseEntity(
            ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}