package com.example.itindr.domain.repository

import com.example.itindr.domain.model.SignInResult
import com.example.itindr.domain.model.SignInCredentials

interface SignInRepository {
    fun signIn(signInCredentials: SignInCredentials) : SignInResult
    fun checkEmailExists(email: String) : Boolean
}
