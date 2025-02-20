package com.qa.socialapi.dto

object KakaoAuthDto {

    data class KakaoAuthResponse(
        val provider: String,
        val accessToken: String,
        val refreshToken: String
    )
}
