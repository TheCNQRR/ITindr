package com.example.chat.ui

import com.example.chat.domain.model.Chat

interface ChatsContract {

    sealed interface State {
        data object Loading : State
        data class  Content(val chats: List<Chat>) : State
        data class Error(val message: String) : State
    }

    sealed interface Event {
        data object LoadChats : Event
        data class OnChatClick(val chatId: String) : Event
    }

    sealed interface Effect {
        data class NavigateToChat(val chatId: String) : Effect
    }
}
