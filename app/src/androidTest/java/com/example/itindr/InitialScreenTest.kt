package com.example.itindr

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class InitialScreenTest : TestCase() {
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testInitialScreen() = run {
        step("Открытие Welcome, ожидание анимаций") {
            flakySafely(timeoutMs = 3000) {
                InitialScreen.logo.isVisible()
            }
        }

        step("Проверка отображения элементов") {
            InitialScreen {
                logo {
                    isVisible()
                    isCompletelyDisplayed()
                }

                subtitle {
                    isVisible()
                    isCompletelyDisplayed()
                    hasText(R.string.initial_screen_text)
                }

                background.isVisible()

                signUpButton {
                    isVisible()
                    isCompletelyDisplayed()
                    isEnabled()
                    hasText(R.string.sign_up)
                }

                signInButton {
                    isVisible()
                    isCompletelyDisplayed()
                    isEnabled()
                    hasText(R.string.sign_in)
                }
            }
        }
    }

    @Test
    fun navigationToMainScreen() = run {
        Intents.init()
        try {
            step("Открытие Welcome, ожидание анимаций") {
                flakySafely(timeoutMs = 3000) {
                    InitialScreen.logo.isVisible()
                }
            }

            step("Нажать кнопку 'Зарегистрироваться'") {
                InitialScreen.signUpButton.click()
            }

            step("Проверить, что открыт экран регистрации") {
                SignUpScreen.signUpButton.isVisible()
            }

            step("Нажать кнопку 'Зарегистрироваться' на экране регистрации") {
                SignUpScreen.signUpButton.click()
            }

            step("Проверить, что открыт экран о себе") {
                AboutMeScreen.saveButton.isVisible()
            }

            step("Нажать кнопку 'Сохранить'") {
                AboutMeScreen.saveButton.click()
            }

            step("Проверить, что открыт главный экран") {
                flakySafely(timeoutMs = 3000) {
                    intended(hasComponent(MainScreenActivity::class.java.name))
                }
            }

        } finally {
            Intents.release()
        }
    }
}
