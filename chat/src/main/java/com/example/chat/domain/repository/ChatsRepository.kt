package com.example.chat.domain.repository

import com.example.chat.domain.model.Chat
import com.example.chat.domain.model.Message
import java.util.UUID

interface ChatsRepository {

    suspend fun getChats(userId: UUID): List<Chat>

    suspend fun getMessages(chatId: UUID): List<Message>
}
