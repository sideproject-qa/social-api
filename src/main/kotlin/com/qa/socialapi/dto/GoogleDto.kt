package com.qa.socialapi.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenResponse(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Int,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("refresh_token") val refreshToken: String?
)

data class GoogleUserInfoResponse(
    val sub: String,
    val name: String,
    val email: String,
    @JsonProperty("picture") val profilePicture: String
)