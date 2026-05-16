package com.example.chat.data

import com.example.chat.R
import com.example.chat.domain.model.Chat
import com.example.chat.domain.model.Message
import com.example.chat.domain.repository.ChatsRepository
import java.util.UUID
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor() : ChatsRepository {

    override suspend fun getChats(userId: UUID): List<Chat> {
        return listOf(
            Chat(
                id = UUID.fromString("00000000-0000-0000-0000-000000000002"),
                name = "Андрей",
                lastName = "Иванов",
                avatarResId = R.drawable.ic_mock_user_photo
            ),
            Chat(
                id = UUID.fromString("00000000-0000-0000-0000-000000000003"),
                name = "Иван",
                lastName = "Сидоров",
                avatarResId = R.drawable.ic_mock_user_photo_2
            ),
        )
    }

    override suspend fun getMessages(chatId: UUID): List<Message> {
        return listOf(
            Message(
                id = UUID.randomUUID(),
                text = "Сообщение 1",
                images = emptyList(),
                timestamp = "15:34 • 16 мая 2026",
                isMine = true
            ),
            Message(
                id = UUID.randomUUID(),
                text = "Сообщение 2",
                images = listOf(R.drawable.ic_mock_user_photo, R.drawable.ic_mock_user_photo_2),
                timestamp = "15:36 • 16 мая 2026",
                isMine = false
            )
        )
    }
}
