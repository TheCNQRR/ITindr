package com.example.itindr.domain.usecase

import com.example.itindr.domain.model.EmailValidationResult
import com.example.itindr.util.isValidEmail

class ValidateEmailUseCase {
    fun execute(email: String): EmailValidationResult {
        val isValid = email.isValidEmail()

        return if (isValid) {
            EmailValidationResult(true, null)
        } else {
            EmailValidationResult(false, "Invalid email")
        }
    }
}
