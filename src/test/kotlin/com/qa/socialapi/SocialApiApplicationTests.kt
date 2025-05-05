package com.qa.socialapi

import com.qa.socialapi.repository.UserEntity
import com.qa.socialapi.repository.UserRepository
import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class SocialApiApplicationTests(private val repository: UserRepository) : FunSpec({

    context("imsi") {
        test("imsi") {
            val user = UserEntity(
                ticket = 0,
                goalPoint = 0,
                currentPoint = 0,
                refreshToken = null,
                platformId = "test",
                platform = "test",
                name = "name",
                email = "email",
                nickname = "nickname"
            )
            repository.save(user)

            repository.findAll().forEach {
                println("user: $it")
            }
        }
    }


})
