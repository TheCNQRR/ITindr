package com.example.itindr.util

import android.content.res.Resources

object Functions {
    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}
