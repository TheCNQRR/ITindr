package com.example.chat.domain.usecase

import com.example.chat.domain.model.Message
import com.example.chat.domain.repository.ChatsRepository
import java.util.UUID
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatsRepository
) {

    suspend fun execute(chatId: UUID) : Result<List<Message>> = try {
        Result.success(repository.getMessages(chatId))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
