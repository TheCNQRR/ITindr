package com.example.itindr.domain.usecase

import com.example.itindr.domain.model.SignInCredentials

class CheckEmptyFieldsUseCase {
    fun execute(signInCredentials: SignInCredentials) : Boolean {
        return !(signInCredentials.email != null && signInCredentials.password != null &&
                signInCredentials.email.isNotBlank() && signInCredentials.password.isNotBlank())

    }
}
