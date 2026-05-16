package com.example.chat.ui

import androidx.lifecycle.ViewModel
import com.example.chat.domain.usecase.GetChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ChatsViewModel(
    private val getChatsUseCase: GetChatsUseCase
) : ViewModel() {

}
