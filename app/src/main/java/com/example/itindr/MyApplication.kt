package com.example.itindr

import android.app.Application
import com.example.itindr.data.repository.SignInRepositoryImpl
import com.example.itindr.domain.usecase.CheckEmptyFieldsUseCase
import com.example.itindr.domain.usecase.SignInUseCase
import com.example.itindr.domain.usecase.ValidateEmailUseCase

class MyApplication : Application() {
    lateinit var signInRepository: SignInRepositoryImpl
    lateinit var checkEmptyFieldsUseCase: CheckEmptyFieldsUseCase
    lateinit var validateEmailUseCase: ValidateEmailUseCase
    lateinit var signInUseCase: SignInUseCase

    override fun onCreate() {
        super.onCreate()

        signInRepository = SignInRepositoryImpl()

        checkEmptyFieldsUseCase = CheckEmptyFieldsUseCase()
        validateEmailUseCase = ValidateEmailUseCase()

        signInUseCase = SignInUseCase(
            checkEmptyFieldsUseCase,
            validateEmailUseCase,
            signInRepository
        )
    }
}
