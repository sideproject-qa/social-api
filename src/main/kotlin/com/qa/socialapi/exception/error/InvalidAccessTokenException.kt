package com.qa.socialapi.exception.error

import com.qa.socialapi.exception.error.http.UnauthorizedException

class InvalidAccessTokenException(message: String): UnauthorizedException(message) {
}