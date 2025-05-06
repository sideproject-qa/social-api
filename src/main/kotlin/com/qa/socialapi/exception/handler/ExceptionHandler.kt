package com.qa.socialapi.exception.handler

import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.exception.error.http.BadRequestException
import com.qa.socialapi.exception.error.http.NotFoundException
import com.qa.socialapi.exception.error.http.UnauthorizedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(BadRequestException::class)
    fun badRequestExceptionHandler(e: BadRequestException): ResponseEntity<ResponseWrapper<Unit>> {
        return wrap(HttpStatus.BAD_REQUEST, data = Unit)
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundExceptionHandler(e: NotFoundException): ResponseEntity<ResponseWrapper<Unit>> {
        return wrap(HttpStatus.NOT_FOUND, data = Unit)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorizedExceptionHandler(e: UnauthorizedException): ResponseEntity<ResponseWrapper<Unit>> {
        return wrap(HttpStatus.UNAUTHORIZED, data = Unit)
    }

    private fun <T> wrap(httpStatus: HttpStatus, message: String = "fail", data: T): ResponseEntity<ResponseWrapper<T>> {
        return ResponseEntity
            .status(httpStatus)
            .body(ResponseWrapper.from(httpStatus = httpStatus, message = message, data = data))
    }
}