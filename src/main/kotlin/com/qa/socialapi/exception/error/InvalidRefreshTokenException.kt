package com.qa.socialapi.exception.error

import com.qa.socialapi.exception.error.http.UnauthorizedException

class InvalidRefreshTokenException(message: String): UnauthorizedException(message)