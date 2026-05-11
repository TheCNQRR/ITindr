package com.example.itindr.domain.usecase

import com.example.itindr.domain.model.SignInCredentials
import com.example.itindr.domain.model.SignInResult
import com.example.itindr.domain.repository.SignInRepository

class SignInUseCase(
    private val checkEmptyFieldsUseCase: CheckEmptyFieldsUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val signInRepository: SignInRepository
) {
    suspend fun execute(signInCredentials: SignInCredentials): SignInResult {
        val checkEmptyFieldsResult = checkEmptyFieldsUseCase.execute(signInCredentials)
        if (checkEmptyFieldsResult) {
            return SignInResult.Error("The input fields contain an empty value")
        }

        val emailValidationResult = validateEmailUseCase.execute(signInCredentials.email!!)
        if (!emailValidationResult.status) {
            return SignInResult.Error(emailValidationResult.errorMessage!!)
        }

        return signInRepository.signIn(signInCredentials)
    }
}
