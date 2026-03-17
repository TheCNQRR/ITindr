package com.example.itindr.util

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
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

fun vibrate(context: Context, duration: Long) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
}

fun View.shake(delta: Float, duration: Long) {
    val animator = ObjectAnimator.ofFloat(this, "translationX", 0f,
        delta, -delta, delta, -delta, 0f)
    animator.duration = duration
    animator.start()
}
