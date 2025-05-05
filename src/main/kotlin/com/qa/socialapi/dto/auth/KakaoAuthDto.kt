package com.qa.socialapi.dto.auth

object KakaoAuthDto {

    data class KakaoAuthRequest(
        val accessToken: String
    )

    data class KakaoAuthResponse(
        val platform: String,
        val accessToken: String,
        val refreshToken: String
    )
}
