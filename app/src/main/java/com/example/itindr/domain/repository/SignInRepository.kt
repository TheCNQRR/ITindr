package com.example.itindr.domain.repository

import com.example.itindr.domain.model.SignInCredentials
import com.example.itindr.domain.model.SignInResult

interface SignInRepository {
    suspend fun signIn(signInCredentials: SignInCredentials): SignInResult
    fun checkEmailExists(email: String): Boolean
}
