package com.qa.socialapi.exception.error

import com.qa.socialapi.exception.error.http.BadRequestException

open class KakaoTokenInvalidException(message: String): BadRequestException(message)