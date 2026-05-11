package com.example.itindr.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.itindr.MyApplication
import com.example.itindr.domain.model.SignInCredentials
import com.example.itindr.domain.model.SignInResult
import com.example.itindr.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignInUiState(
    val email: String? = null,
    val password: String? = null,
    val isSuccess: Boolean? = null,
    val errorMessage: String? = null
)

class SignInViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun signIn() {
        val currentState = _uiState.value
        val email = currentState.email ?: ""
        val password = currentState.password ?: ""

        val signInCredentials = SignInCredentials(email, password)

        viewModelScope.launch {
            val result = signInUseCase.execute(signInCredentials)

            _uiState.update { it.copy(
                isSuccess = result is SignInResult.Success,
                errorMessage = (result as? SignInResult.Error)?.message
            ) }
        }
    }

    fun clearSignInState() {
        _uiState.update { it.copy(
            isSuccess = null,
            errorMessage = null
        ) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY]) as MyApplication
                SignInViewModel(application.signInUseCase)
            }
        }
    }
}
