package com.qa.socialapi.dto

object GoogleAuthDto {

    data class GoogleAuthResponse(
        val provider: String,
        val accessToken: String,
        val refreshToken: String
    )
}
