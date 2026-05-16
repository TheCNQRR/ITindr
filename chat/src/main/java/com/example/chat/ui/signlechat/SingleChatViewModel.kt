package com.example.chat.ui.signlechat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.domain.usecase.GetMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class SingleChatViewModel(
    private val getMessagesUseCase: GetMessagesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val chatId: UUID = savedStateHandle.get<String>("chatId")?.let { UUID.fromString(it) }
        ?: error("chatId is required")

    private val _state = MutableStateFlow<ChatContract.State>(ChatContract.State.Loading)
    val state: StateFlow<ChatContract.State> = _state.asStateFlow()

    private val _effect = Channel<ChatContract.Effect>()
    val effect = _effect.receiveAsFlow()


    init {
        dispatchEvent(ChatContract.Event.LoadMessages)
    }

    private fun dispatchEvent(event: ChatContract.Event) {
        when (event) {
            ChatContract.Event.LoadMessages -> loadMessages()
            is ChatContract.Event.OnBackClick ->
                sendEffect(ChatContract.Effect.NavigateBack(event.chatId))
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _state.value = ChatContract.State.Loading
            val result = getMessagesUseCase.execute(chatId)

            result.fold(
                onSuccess = { chats ->
                    _state.value = ChatContract.State.Content(chats)
                },
                onFailure = { error ->
                    _state.value = ChatContract.State.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    private fun sendEffect(effect: ChatContract.Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
