package com.qa.socialapi.dto.auth

import com.qa.socialapi.dto.ResponseWrapper

object GoogleAuthDto {

    data class GoogleAuthRequest(
        val idToken: String
    )

    data class GoogleAuthResponse(
        val platform: String,
        val accessToken: String,
        val refreshToken: String
    ) {
        fun toResponseWrapper(code: Int): ResponseWrapper<GoogleAuthResponse> {
            return ResponseWrapper(
                code = code,
                message = "success",
                data = this
            )
        }
    }
}
