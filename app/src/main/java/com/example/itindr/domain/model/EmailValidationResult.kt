package com.example.itindr.domain.model

data class EmailValidationResult(
    val status: Boolean,
    val errorMessage: String?
)
