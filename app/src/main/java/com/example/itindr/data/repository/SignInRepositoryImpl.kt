package com.example.itindr.data.repository

import com.example.itindr.domain.model.SignInResult
import com.example.itindr.domain.model.SignInCredentials
import com.example.itindr.domain.repository.SignInRepository

class SignInRepositoryImpl : SignInRepository {
    override fun signIn(signInCredentials: SignInCredentials) : SignInResult {
        return if (signInCredentials.email.contains("successTestEmail")) {
            SignInResult.Success("auth-token-1111")
        } else {
            SignInResult.Error("Invalid credentials")
        }
    }

    override fun checkEmailExists(email: String): Boolean {
        return email.contains("successTestEmail")
    }
}
