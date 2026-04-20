package com.example.itindr.domain.usecase

import com.example.itindr.domain.repository.StreamRepository

class ProccessReactionUseCase(private val streamRepository: StreamRepository) {
    suspend fun execute(personId: String, isLike: Boolean) {
        streamRepository.proccessReaction(personId, isLike)
    }
}
