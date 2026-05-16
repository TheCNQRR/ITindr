package com.example.chat.ui.signlechat

import com.example.chat.domain.model.Message

interface ChatContract {

    sealed interface State {
        data object Loading : State
        data class  Content(val messages: List<Message>) : State
        data class Error(val message: String) : State
    }

    sealed interface Event {
        data object LoadMessages : Event
        data class OnBackClick(val chatId: String) : Event
    }

    sealed interface Effect {
        data class NavigateBack(val chatId: String) : Effect
    }
}
