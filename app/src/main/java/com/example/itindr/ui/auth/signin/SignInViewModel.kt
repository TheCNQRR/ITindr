package com.example.itindr.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.itindr.MyApplication
import com.example.itindr.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class signInUiState(
    val email: String? = null,
    val password: String? = null
)

class SignInViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(signInUiState())
    val uiState: StateFlow<signInUiState> = _uiState.asStateFlow()

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY]) as MyApplication

                return SignInViewModel(
                    application.signInUseCase
                ) as T
            }
        }
    }
}
