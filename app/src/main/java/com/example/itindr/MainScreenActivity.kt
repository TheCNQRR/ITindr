package com.example.itindr

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        supportActionBar?.hide()

        val mockPerson = Person("Вадик", "Люблю программировать на питоне. Люблю изучать питон. Люблю всё, что угодно, связанное с питоном. А еще я люблю перл.",
            listOf("Python", "Django", "REST"), R.drawable.ic_mock_user_photo)

        setContent {
            MainScreen(mockPerson)
        }
    }

    data class Person(
        val name: String,
        val bio: String,
        val tags: List<String>,
        val photoUrl: Int
    )
}
