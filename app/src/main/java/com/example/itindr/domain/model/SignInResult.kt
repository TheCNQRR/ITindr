package com.example.itindr.domain.model

sealed class SignInResult {
    data class Success(val token: String) : SignInResult()
    data class Error(val message: String) : SignInResult()
}
