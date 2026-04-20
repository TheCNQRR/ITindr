package com.example.itindr.ui.stream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itindr.R
import com.example.itindr.ui.MainScreenActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class StreamViewModel : ViewModel() {
    private val _state = MutableStateFlow<StreamContract.StreamState>(StreamContract.StreamState.Loading)
    val state: StateFlow<StreamContract.StreamState> = _state.asStateFlow()

    private val _effect = Channel<StreamContract.StreamEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        dispatchEvent(StreamContract.StreamEvent.LoadPerson)
    }

    fun dispatchEvent(event: StreamContract.StreamEvent) {
        when (event) {
            is StreamContract.StreamEvent.LoadPerson -> loadNextPerson()
            is StreamContract.StreamEvent.OnLikeClick -> proccessReaction(isLike = true)
            is StreamContract.StreamEvent.OnDislikeClick -> proccessReaction(isLike = false)
            is StreamContract.StreamEvent.OnPeopleNavigationClick ->
                sendEffect(StreamContract.StreamEffect.NavigateToPeople)
            is StreamContract.StreamEvent.OnChatsNavigationClick ->
                sendEffect(StreamContract.StreamEffect.NavigateToChats)
            is StreamContract.StreamEvent.OnProfileNavigationClick ->
                sendEffect(StreamContract.StreamEffect.NavigateToProfile)
        }
    }

    private fun loadNextPerson() {

    }

    private fun proccessReaction(isLike: Boolean) {
        dispatchEvent(StreamContract.StreamEvent.LoadPerson)
    }

    private fun sendEffect(effect: StreamContract.StreamEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
