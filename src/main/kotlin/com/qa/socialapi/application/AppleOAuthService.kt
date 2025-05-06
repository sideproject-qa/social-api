package com.qa.socialapi.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.qa.socialapi.dto.auth.AppleAuthDto.AppleAuthResponse
import com.qa.socialapi.enum.Platform
import com.qa.socialapi.exception.error.AppleTokenInvalidException
import com.qa.socialapi.repository.UserEntity
import com.qa.socialapi.util.JwtUtil
import io.jsonwebtoken.Jwts
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*

@Service
class AppleOAuthService(
    val userService: UserService,
    val jwtUtil: JwtUtil,
    @Value("\${apple.public-key-url}") private val publicKeyUrl: String
) {

    private val logger = KotlinLogging.logger {}
    val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun handleApple(idToken: String): Pair<Boolean, AppleAuthResponse> {
        val userInfo = verifyAppleIdToken(idToken)
        val isNewUser: Boolean
        val user: UserEntity

        val existingUser = userService.findByPlatformId(userInfo.subId)
        if (existingUser == null) {
            user = userService.save(
                UserEntity(
                    platform = Platform.APPLE,
                    platformId = userInfo.subId,
                    email = userInfo.email,
                )
            )
            isNewUser = true
        } else {
            user = existingUser
            isNewUser = false
        }

        val refreshToken = jwtUtil.generateRefreshToken(user.id.toString())
        userService.save(user.copy(refreshToken = refreshToken))

        return Pair(isNewUser, AppleAuthResponse(
            platform = Platform.APPLE.name,
            accessToken = jwtUtil.generateAccessToken(user.id.toString()),
            refreshToken = refreshToken
        ))
    }

    private fun verifyAppleIdToken(idToken: String): AppleAuthInfo {
        val parts = idToken.split(".")
        if (parts.size != 3) {
            throw AppleTokenInvalidException("Invalid Apple Id Token")
        }

        val headerJson = String(Base64.getDecoder().decode(parts[0]))
        val header: Map<*, *> = objectMapper.readValue(headerJson, Map::class.java) as Map<*, *>
        val kid = header["kid"] as? String ?: throw AppleTokenInvalidException("No kid found in token header")

        val appleKeys = getApplePublicKeys()
        logger.info { "appleKeys: $appleKeys" }
        val appleKey =
            appleKeys.find { it.kid == kid } ?: throw AppleTokenInvalidException("Matching public key not found")
        val publicKey = generatePublicKey(appleKey.n, appleKey.e)

        val claims = Jwts.parser()
            .verifyWith(publicKey)
            .build()
            .parseSignedClaims(idToken)
            .payload

        logger.info { "claims: $claims" }

        val subId = claims["sub"] as? String ?: throw AppleTokenInvalidException("subId not found in token claims")
        val email = claims["email"] as? String

        return AppleAuthInfo(subId, email)
    }

    private fun getApplePublicKeys(): List<ApplePublicKey> {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(publicKeyUrl))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) {
            throw AppleTokenInvalidException("Failed to fetch Apple public keys, status: ${response.statusCode()}")
        }
        val responseBody = response.body()
        logger.info { "Apple public keys: $responseBody" }
        val keysResponse = objectMapper.readValue(responseBody, ApplePublicKeyResponse::class.java)
        return keysResponse.keys
    }

    private fun generatePublicKey(n: String, e: String): PublicKey {
        val nBytes = Base64.getUrlDecoder().decode(n)
        val eBytes = Base64.getUrlDecoder().decode(e)
        val modules = BigInteger(1, nBytes)
        val exponents = BigInteger(1, eBytes)
        val spec = RSAPublicKeySpec(modules, exponents)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(spec)
    }

    data class ApplePublicKeyResponse(
        val keys: List<ApplePublicKey>
    )

    data class ApplePublicKey(
        val kty: String,
        val kid: String,
        val use: String,
        val alg: String,
        val n: String,
        val e: String
    )

    data class AppleAuthInfo(
        val subId: String,
        val email: String?
    )
}