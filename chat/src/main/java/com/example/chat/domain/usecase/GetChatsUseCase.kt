package com.example.chat.domain.usecase

import com.example.chat.domain.model.Chat
import com.example.chat.domain.repository.ChatsRepository
import java.util.UUID
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val repository: ChatsRepository
) {

    suspend fun execute(userId: UUID) : Result<List<Chat>> = try {
        Result.success(repository.getChats(userId))
    } catch (e: Exception) {
        Result.failure(e);
    }
}
