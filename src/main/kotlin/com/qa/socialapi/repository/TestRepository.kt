package com.qa.socialapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TestRepository: JpaRepository<TestEntity, UUID>