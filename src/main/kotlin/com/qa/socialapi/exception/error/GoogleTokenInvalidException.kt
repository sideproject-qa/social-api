package com.qa.socialapi.exception.error

import com.qa.socialapi.exception.error.http.BadRequestException

class GoogleTokenInvalidException(message: String): BadRequestException(message)