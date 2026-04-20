package com.example.itindr.ui.stream

sealed class StreamEffect {
    data object NavigateToPeople: StreamEffect()
    data object NavigateToChats: StreamEffect()
    data object NavigateToProfile: StreamEffect()
}
