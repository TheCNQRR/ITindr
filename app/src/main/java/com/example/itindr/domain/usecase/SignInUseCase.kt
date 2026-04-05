package com.example.itindr.domain.usecase

import com.example.itindr.domain.model.SignInCredentials
import com.example.itindr.domain.model.SignInResult
import com.example.itindr.domain.repository.SignInRepository

class SignInUseCase(
    private val checkEmptyFieldsUseCase: CheckEmptyFieldsUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase,
    private val signInRepository: SignInRepository
) {
    suspend fun execute(signInCredentials: SignInCredentials) : SignInResult {
        val checkEmptyFieldsResult = checkEmptyFieldsUseCase.execute(signInCredentials)
        if (checkEmptyFieldsResult) {
            return SignInResult.Error("Credentials contains empty fields")

        }

        val emailValidationResult = validateEmailUseCase.execute(signInCredentials.email!!)
        if (!emailValidationResult.status) {
            return SignInResult.Error(emailValidationResult.errorMessage!!)
        }

        val emailExistsResult = checkEmailExistsUseCase.execute(signInCredentials.email)
        if (!emailExistsResult) {
            return SignInResult.Error("Email does not exists")
        }

        return signInRepository.signIn(signInCredentials)
    }
}