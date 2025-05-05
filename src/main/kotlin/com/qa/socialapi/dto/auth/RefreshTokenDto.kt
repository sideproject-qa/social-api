package com.qa.socialapi.dto.auth

object RefreshTokenDto {
    data class RefreshTokenRequest(
        val refreshToken: String
    )

    data class RefreshTokenResponse(
        val accessToken: String
    )
}