package com.qa.socialapi.exception.error

import com.qa.socialapi.exception.error.http.NotFoundException

class AppNotFoundException(message: String): NotFoundException(message)