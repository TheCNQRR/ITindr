package com.example.itindr.util

import android.content.res.Resources
import android.util.Patterns

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
