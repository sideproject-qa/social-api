package com.qa.socialapi.util

import com.qa.socialapi.exception.error.InvalidAccessTokenException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val accessSecret: String,
    @Value("\${jwt.refresh-secret}") private val refreshSecret: String
) {
    private val accessSecretKey: SecretKey = Keys.hmacShaKeyFor(accessSecret.toByteArray())
    private val refreshSecretKey: SecretKey = Keys.hmacShaKeyFor(refreshSecret.toByteArray())
    private val accessTokenExpiration: Long = 60 * 60 * 1000 // 1시간 (밀리초)
    private val refreshTokenExpiration: Long = 7 * 24 * 60 * 60 * 1000 // 7일

    private val logger = KotlinLogging.logger {}

    fun generateAccessToken(userId: String): String {
        return Jwts.builder()
            .subject(userId)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + accessTokenExpiration))
            .signWith(accessSecretKey)
            .compact()
    }

    fun generateRefreshToken(userId: String): String {
        return Jwts.builder()
            .subject(userId)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + refreshTokenExpiration))
            .signWith(refreshSecretKey)
            .compact()
    }

    fun validateAccessToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(accessSecretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            logger.error { "Invalid JWT token: $e" }
            return false
        }
    }


    fun validateRefreshToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(refreshSecretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            logger.error { "Invalid JWT token: $e" }
            false
        }
    }

    fun getUserIdFromToken(token: String, isRefreshToken: Boolean = false): String? {
        val key = if (isRefreshToken) refreshSecretKey else accessSecretKey
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        } catch (e: Exception) {
            null
        }
    }
}