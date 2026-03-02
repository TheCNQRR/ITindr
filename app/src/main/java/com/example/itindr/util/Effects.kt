package com.example.itindr.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

class Effects {
    companion object {
        @SuppressLint("ClickableViewAccessibility")
        fun setPressEffect(view: View, onAction: () -> Unit) {
            view.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.animate().cancel()

                        v.animate()
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                            .alpha(0.7f)
                            .setStartDelay(0)
                            .setDuration(80)
                            .start()
                    }

                    MotionEvent.ACTION_UP -> {
                        v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(80)
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
                            .setDuration(80)
                            .start()
                    }
                }
                true
            }
        }
    }
}