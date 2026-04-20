package com.example.itindr.ui.stream

sealed class StreamEvent {
    data object LoadPerson: StreamEvent()
    data object OnLikeClick: StreamEvent()
    data object OnDislikeClick: StreamEvent()
}
