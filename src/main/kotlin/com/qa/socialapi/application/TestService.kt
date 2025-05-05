package com.qa.socialapi.application

import com.qa.socialapi.dto.test.CreateTestDto.CreateTestRequest
import com.qa.socialapi.exception.error.PilotTestNotFoundException
import com.qa.socialapi.repository.TestEntity
import com.qa.socialapi.repository.TestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TestService(
    private val repository: TestRepository
) {
    fun findById(id: UUID): TestEntity {
        return repository.findByIdOrNull(id) ?: throw PilotTestNotFoundException("$id pilot test not found")
    }

    fun findAll(): List<TestEntity> {
        return repository.findAll()
    }

    fun save(dto: CreateTestRequest): TestEntity {
        return repository.save(TestEntity.create(dto))
    }

    fun deleteById(id: UUID) {
        repository.deleteById(id)
    }
}