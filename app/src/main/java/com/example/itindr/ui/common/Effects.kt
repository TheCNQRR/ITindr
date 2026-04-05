package com.example.itindr.ui.common

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
private const val SMALL_SCALE = 0.95f
private const val SMALL_ALPHA = 0.7f
private const val DURATION = 80L

@SuppressLint("ClickableViewAccessibility")
fun setPressEffect(view: View, onAction: () -> Unit) {
    view.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.animate().cancel()

                v.animate()
                    .scaleX(SMALL_SCALE)
                    .scaleY(SMALL_SCALE)
                    .alpha(SMALL_ALPHA)
                    .setStartDelay(0)
                    .setDuration(DURATION)
                    .start()
            }

            MotionEvent.ACTION_UP -> {
                v.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(DURATION)
                    .withEndAction {
                        onAction()
                    }
                    .start()
            }

            MotionEvent.ACTION_CANCEL -> {
                v.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(DURATION)
                    .start()
            }
        }
        true
    }
}
