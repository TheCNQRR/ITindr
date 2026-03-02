package com.example.itindr.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

object Effects {
    private const val ZERO = 0L
    private const val SMALL_SCALE = 0.95f
    private const val NORMAL_SCALE = 1f
    private const val SMALL_ALPHA = 0.7f
    private const val NORMAL_ALPHA = 1f
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
                        .setStartDelay(ZERO)
                        .setDuration(DURATION)
                        .start()
                }

                MotionEvent.ACTION_UP -> {
                    v.animate()
                        .scaleX(NORMAL_SCALE)
                        .scaleY(NORMAL_SCALE)
                        .alpha(NORMAL_ALPHA)
                        .setDuration(DURATION)
                        .withEndAction {
                            onAction()
                        }
                        .start()
                }

                MotionEvent.ACTION_CANCEL -> {
                    v.animate()
                        .scaleX(NORMAL_SCALE)
                        .scaleY(NORMAL_SCALE)
                        .alpha(NORMAL_ALPHA)
                        .setDuration(DURATION)
                        .start()
                }
            }
            true
        }
    }
}
