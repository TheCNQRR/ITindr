package com.example.itindr

import android.app.Application
import com.example.itindr.data.repository.SignInRepositoryImpl
import com.example.itindr.domain.usecase.CheckEmailExistsUseCase
import com.example.itindr.domain.usecase.CheckEmptyFieldsUseCase
import com.example.itindr.domain.usecase.SignInUseCase
import com.example.itindr.domain.usecase.ValidateEmailUseCase

class MyApplication : Application() {
    lateinit var signInRepository: SignInRepositoryImpl
    lateinit var checkEmailExistsUseCase: CheckEmailExistsUseCase
    lateinit var checkEmptyFieldsUseCase: CheckEmptyFieldsUseCase
    lateinit var validateEmailUseCase: ValidateEmailUseCase
    lateinit var signInUseCase: SignInUseCase

    override fun onCreate() {
        super.onCreate()

        signInRepository = SignInRepositoryImpl()

        checkEmptyFieldsUseCase = CheckEmptyFieldsUseCase()
        validateEmailUseCase = ValidateEmailUseCase()
        checkEmailExistsUseCase = CheckEmailExistsUseCase(signInRepository)

        signInUseCase = SignInUseCase(
            checkEmptyFieldsUseCase,
            validateEmailUseCase,
            checkEmailExistsUseCase,
            signInRepository
        )
    }
}
