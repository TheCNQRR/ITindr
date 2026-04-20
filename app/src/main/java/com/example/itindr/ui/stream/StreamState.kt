package com.example.itindr.ui.stream

import com.example.itindr.ui.MainScreenActivity

sealed class StreamState {
    data object Loading: StreamState()
    data class Content(val person: MainScreenActivity.Person): StreamState()
    data class Error(val message: String): StreamState()
}
