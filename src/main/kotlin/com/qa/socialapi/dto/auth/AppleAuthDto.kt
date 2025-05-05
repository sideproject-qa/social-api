package com.qa.socialapi.dto.auth

object AppleAuthDto {

    data class AppleAuthRequest(
        val idToken: String
    )

    data class AppleAuthResponse(
        val platform: String,
        val accessToken: String,
        val refreshToken: String
    )
}