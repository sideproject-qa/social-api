package com.qa.socialapi.exception.handler

import org.springframework.http.HttpStatus
import java.time.ZoneId
import java.time.ZonedDateTime

data class ErrorResponse(
    val code: HttpStatus,
    val message: String,
    val timestamp: String = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString()
)