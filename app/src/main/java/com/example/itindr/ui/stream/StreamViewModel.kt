package com.example.itindr.ui.stream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.itindr.MyApplication
import com.example.itindr.R
import com.example.itindr.domain.usecase.LoadNextPersonUseCase
import com.example.itindr.domain.usecase.ProccessReactionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class StreamViewModel(
    private val loadNextPersonUseCase: LoadNextPersonUseCase,
    private val proccessReactionUseCase: ProccessReactionUseCase
) : ViewModel() {
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
        viewModelScope.launch {
            _state.value = StreamContract.StreamState.Loading
            val result = loadNextPersonUseCase.execute()

            result.fold(
                onSuccess = { person ->
                    _state.value = StreamContract.StreamState.Content(person)
                },
                onFailure = { error ->
                    _state.value = StreamContract.StreamState.Error(
                        (error.message ?: R.string.person_load_error).toString()
                    )
                }
            )
        }
    }

    private fun proccessReaction(isLike: Boolean) {
        val currentState = _state.value
        if (currentState is StreamContract.StreamState.Content) {
            viewModelScope.launch {
                proccessReactionUseCase.execute(currentState.person.name, isLike)
                dispatchEvent(StreamContract.StreamEvent.LoadPerson)
            }
        }
    }

    private fun sendEffect(effect: StreamContract.StreamEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY]) as MyApplication
                val container = application.appContainer
                StreamViewModel(
                    loadNextPersonUseCase = container.loadNextPersonUseCase,
                    proccessReactionUseCase = container.proccessReactionUseCase
                )
            }
        }
    }
}
