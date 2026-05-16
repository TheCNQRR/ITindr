package com.example.chat.ui.signlechat

import androidx.lifecycle.ViewModel
import com.example.chat.domain.usecase.GetChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SingleChatViewModel(
    private val getChatsUseCase: GetChatsUseCase
) : ViewModel() {

}
