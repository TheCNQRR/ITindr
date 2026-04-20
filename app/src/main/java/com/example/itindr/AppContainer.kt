package com.example.itindr

import com.example.itindr.data.repository.SignInRepositoryImpl
import com.example.itindr.data.repository.StreamRepositoryImpl
import com.example.itindr.domain.usecase.CheckEmptyFieldsUseCase
import com.example.itindr.domain.usecase.LoadNextPersonUseCase
import com.example.itindr.domain.usecase.ProccessReactionUseCase
import com.example.itindr.domain.usecase.SignInUseCase
import com.example.itindr.domain.usecase.ValidateEmailUseCase

class AppContainer {
    private val streamRepository by lazy {
        StreamRepositoryImpl()
    }

    private val signInRepository by lazy { SignInRepositoryImpl() }

    val loadNextPersonUseCase by lazy {
        LoadNextPersonUseCase(streamRepository)
    }

    val proccessReactionUseCase by lazy {
        ProccessReactionUseCase(streamRepository)
    }

    val signInUseCase by lazy {
        SignInUseCase(
            CheckEmptyFieldsUseCase(),
            ValidateEmailUseCase(),
            signInRepository
        )
    }
}
