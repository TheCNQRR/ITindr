package com.example.itindr

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest : TestCase() {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testBackButtonReturnsToInitialScreen() = run {
        step("Открыть экран Welcome") {
            flakySafely {
                InitialScreen.logo.isVisible()
            }
        }

        step("Нажать кнопку 'Зарегистрироваться'") {
            InitialScreen.signUpButton.click()
        }

        step("Проверить, что открыт экран регистрации; нажать кнопку 'назад'") {
            SignUpScreen.backButton.isVisible()
            SignUpScreen.backButton.click()
        }

        step("Проверить, что произошёл возврат на экран Welcome") {
            InitialScreen.logo.isVisible()
            InitialScreen.subtitle.isVisible()
            InitialScreen.background.isVisible()
            InitialScreen.signInButton.isVisible()
            InitialScreen.signUpButton.isVisible()
        }
    }
}
