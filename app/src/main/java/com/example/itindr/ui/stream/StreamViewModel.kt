package com.example.itindr.ui.stream

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StreamViewModel : ViewModel() {
    private val _state = MutableStateFlow<StreamState>(StreamState.Loading)
    val state: StateFlow<StreamState> = _state.asStateFlow()
}
