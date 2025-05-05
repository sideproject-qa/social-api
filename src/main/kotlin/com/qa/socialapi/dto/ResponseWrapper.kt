package com.qa.socialapi.dto

import org.springframework.http.HttpStatus

data class ResponseWrapper<T>(
    val code: Int,
    val message: String,
    val data: T
) {
    companion object {
        fun <T> from(httpStatus: HttpStatus, message: String = "success", data: T): ResponseWrapper<T> {
            return ResponseWrapper(
                code = httpStatus.value(),
                message = message,
                data = data
            )
        }
    }
}