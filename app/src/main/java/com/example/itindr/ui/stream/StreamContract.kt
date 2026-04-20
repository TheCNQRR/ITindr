package com.example.itindr.ui.stream

import com.example.itindr.ui.MainScreenActivity

interface StreamContract {
    sealed interface StreamState {
        data object Loading : StreamState
        data class Content(val person: MainScreenActivity.Person) : StreamState
        data class Error(val message: String) : StreamState
    }

    sealed interface StreamEvent {
        data object LoadPerson : StreamEvent
        data object OnLikeClick : StreamEvent
        data object OnDislikeClick : StreamEvent
        data object OnPeopleNavigationClick : StreamEvent
        data object OnChatsNavigationClick : StreamEvent
        data object OnProfileNavigationClick : StreamEvent
    }

    sealed interface StreamEffect {
        data object NavigateToPeople : StreamEffect
        data object NavigateToChats : StreamEffect
        data object NavigateToProfile : StreamEffect
    }
}
