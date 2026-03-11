package com.example.itindr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
    }
}
