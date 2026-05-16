package com.example.chat.domain.model

import java.util.UUID

data class Chat(
    val id: UUID,
    val name: String,
    val lastName: String,
    val avatarResId: Int
)
