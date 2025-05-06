package com.qa.socialapi.application

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.qa.socialapi.dto.auth.GoogleAuthDto.GoogleAuthResponse
import com.qa.socialapi.enum.OSType
import com.qa.socialapi.enum.Platform
import com.qa.socialapi.exception.error.GoogleTokenInvalidException
import com.qa.socialapi.repository.UserEntity
import com.qa.socialapi.util.JwtUtil
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.*

@Service
class GoogleOAuthService(
    private val restTemplate: RestTemplate,
    private val userService: UserService,
    private val jwtUtil: JwtUtil,

    @Value("\${google.client-id-android}") private val clientIdAndroid: String,
    @Value("\${google.client-id-ios}") private val clientIdIos: String,
    @Value("\${google.token-info-url}") private val tokenInfoUrl: String,
) {
    private val logger = KotlinLogging.logger {}

    fun handleGoogle(idToken: String, osType: OSType): Pair<Boolean, GoogleAuthResponse> {
        val authInfo = verifyGoogleIdToken(idToken, osType)
        val isNewUser: Boolean
        val existingUser = userService.findByPlatformId(authInfo.platformId)
        val user: UserEntity

        if (existingUser == null) {
            user = userService.save(
                UserEntity(
                    platform = Platform.GOOGLE,
                    platformId = authInfo.platformId,
                    email = authInfo.email,
                    nickname = authInfo.name
                )
            )
            isNewUser = true
        } else {
            user = existingUser
            isNewUser = false
        }

        val refreshToken = jwtUtil.generateRefreshToken(user.id.toString())
        userService.save(user.copy(refreshToken = refreshToken))

        return Pair(
            isNewUser, GoogleAuthResponse(
                platform = Platform.GOOGLE.name,
                accessToken = jwtUtil.generateAccessToken(user.id.toString()),
                refreshToken = refreshToken
            )
        )
    }


    fun verifyGoogleIdToken(idToken: String, osType: OSType): GoogleUserInfo {
        val url = "$tokenInfoUrl?id_token=$idToken"
        val response = runCatching {
            restTemplate.getForEntity(url, JsonNode::class.java)
        }.onFailure {
            logger.error { "response: $it" }
            throw GoogleTokenInvalidException("Google token verification failed: no response body")
        }.getOrThrow()

        if (response.statusCode == HttpStatus.OK) {
            val body =
                response.body
                    ?: throw GoogleTokenInvalidException("Google token verification failed: no response body")

            val issuer = body["iss"].asText()
            val audience = body["aud"].asText()

            val validClientId = when (osType) {
                OSType.ANDROID -> clientIdAndroid
                OSType.IOS -> clientIdIos
            }

            if (issuer != "https://accounts.google.com" || audience != validClientId) {
                throw GoogleTokenInvalidException("Invalid Google ID Token")
            }

            val googleId = body["sub"].asText()
            val email = body["email"]?.asText()
            val displayName = body["name"]?.asText()
            val picture = body["picture"]?.asText()
            val givenName = body["given_name"]?.asText()
            val familyName = body["family_name"]?.asText()

            logger.info { "Google ID Token Verified - sub: $googleId, email: $email, name: $displayName" }
            return GoogleUserInfo(
                platformId = googleId,
                email = email,
                name = displayName,
                profilePicture = picture
            )
        } else {
            throw GoogleTokenInvalidException("Failed to verify Google ID Token: ${response.statusCode}")
        }
    }

    companion object {
        data class GoogleUserInfo(
            val platformId: String,
            val name: String?,
            val email: String?,
            @JsonProperty("picture") val profilePicture: String?
        )
    }
}
