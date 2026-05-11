package com.example.itindr

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class SignInScreenTest : TestCase() {
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun backButtonReturnsToInitialScreen() = run {
        step("Открыть экран Welcome") {
            flakySafely {
                InitialScreen.logo.isVisible()
            }
        }

        step("Нажать кнопку 'Войти'") {
            InitialScreen.signInButton.click()
        }

        step("Проверить, что открыт экран входа; нажать кнопку 'назад'") {
            SignInScreen.backButton.isVisible()
            SignInScreen.backButton.click()
        }

        step("Проверить, что произошёл возврат на экран Welcome") {
            InitialScreen.logo.isVisible()
            InitialScreen.subtitle.isVisible()
            InitialScreen.background.isVisible()
            InitialScreen.signInButton.isVisible()
            InitialScreen.signUpButton.isVisible()
        }
    }

    @Test
    fun systemBackReturnsToInitialScreen() = run {
        step("Открыть экран Welcome") {
            flakySafely {
                InitialScreen.logo.isVisible()
            }
        }

        step("Нажать кнопку 'Войти'") {
            InitialScreen.signInButton.click()
        }

        step("Проверить, что открыть экран входа, нажать системную кнопку 'назад'") {
            SignInScreen.backButton.isVisible()
            device.uiDevice.pressBack()
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
