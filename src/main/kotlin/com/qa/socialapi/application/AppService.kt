package com.qa.socialapi.application

import com.qa.socialapi.dto.app.CreateAppDto.CreateAppRequest
import com.qa.socialapi.exception.error.AppNotFoundException
import com.qa.socialapi.repository.AppEntity
import com.qa.socialapi.repository.AppRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppService(
    private val repository: AppRepository
) {

    fun save(dto: CreateAppRequest): AppEntity {
        return repository.save(AppEntity.create(dto))
    }

    fun getById(id: UUID): AppEntity {
        return repository.findByIdOrNull(id) ?: throw AppNotFoundException("$id app not found")
    }

    fun deleteById(id: UUID) {
        repository.deleteById(id)
    }
}