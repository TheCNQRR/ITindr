package com.example.chat.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.domain.usecase.GetChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
@Suppress("Unused")
class ChatsViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: UUID = savedStateHandle.get<String>("userId")?.let { UUID.fromString(it) }
        ?: error("userId is required")

    private val _state = MutableStateFlow<ChatsContract.State>(ChatsContract.State.Loading)
    val state: StateFlow<ChatsContract.State> = _state.asStateFlow()

    private val _effect = Channel<ChatsContract.Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        dispatchEvent(ChatsContract.Event.LoadChats)
    }

    fun dispatchEvent(event: ChatsContract.Event) {
        when (event) {
            ChatsContract.Event.LoadChats -> loadChats()
            is ChatsContract.Event.OnChatClick ->
                sendEffect(ChatsContract.Effect.NavigateToChat(event.chatId))
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            _state.value = ChatsContract.State.Loading
            val result = getChatsUseCase.execute(userId)

            result.fold(
                onSuccess = { chats ->
                    _state.value = ChatsContract.State.Content(chats)
                },
                onFailure = { error ->
                    _state.value = ChatsContract.State.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    private fun sendEffect(effect: ChatsContract.Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
