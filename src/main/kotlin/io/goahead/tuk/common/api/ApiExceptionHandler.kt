package io.goahead.tuk.common.api

import io.goahead.tuk.author.exception.AuthorNotFoundException
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ConfessionNotFoundException::class, AuthorNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(exception: RuntimeException): ErrorResponse {
        return ErrorResponse(message = exception.message ?: "Not found")
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(exception: IllegalArgumentException): ErrorResponse {
        return ErrorResponse(message = exception.message ?: "Bad request")
    }
}

data class ErrorResponse(
    val message: String,
)
