package com.example.chat.domain.model

import java.util.UUID

data class Message(
    val id: UUID,
    val text: String?,
    val images: List<Int> = emptyList(),
    val timestamp: String,
    val isMine: Boolean
)
