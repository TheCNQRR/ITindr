package com.example.itindr.util

import android.content.res.Resources

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")
    return this.matches(emailRegex)
}
