package com.example.itindr.domain.usecase

import com.example.itindr.domain.repository.SignInRepository

class CheckEmailExistsUseCase(private val signInRepository: SignInRepository) {
    fun execute(email: String): Boolean {
        return signInRepository.checkEmailExists(email)
    }
}
