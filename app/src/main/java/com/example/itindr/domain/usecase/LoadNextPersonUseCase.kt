package com.example.itindr.domain.usecase

import com.example.itindr.domain.repository.StreamRepository
import com.example.itindr.ui.MainScreenActivity

@Suppress("TooGenericExceptionCaught")
class LoadNextPersonUseCase(private val streamRepository: StreamRepository) {
    suspend fun execute(): Result<MainScreenActivity.Person> {
        return try {
            Result.success(streamRepository.loadNextPerson())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
