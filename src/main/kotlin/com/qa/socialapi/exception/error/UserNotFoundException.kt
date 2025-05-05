package com.qa.socialapi.exception.error

import com.qa.socialapi.exception.error.http.NotFoundException

class UserNotFoundException(message: String): NotFoundException(message)