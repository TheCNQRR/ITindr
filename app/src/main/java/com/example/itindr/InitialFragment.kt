package com.example.itindr

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.itindr.databinding.FragmentInitialBinding
import com.example.itindr.util.Effects

private const val ZERO_ALPHA = 0f
private const val NORMAL_ALPHA = 1f
private const val DURATION_500 = 500L
private const val DURATION_700 = 700L
private const val DURATION_800 = 800L
private const val DELAY_200 = 200L
private const val DELAY_350 = 350L
private const val DELAY_500 = 500L
private const val DELAY_650 = 650L
private const val DELAY_2000 = 2000L
private const val ZERO_TRANSLATION_Y = 0f
private const val TRANSLATION_Y = 200f
private const val TRANSLATION_Y_NEGATIVE = -200f
private const val NORMAL_SCALE = 1f
private const val BIG_SCALE = 1.2f

class InitialFragment : Fragment() {
    private var _binding: FragmentInitialBinding? = null
    private val binding get() = _binding!!

    private var heartAnimatorSet: AnimatorSet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setupEntranceAnimations()
        startHeartbeatAnimation()
    }

    private fun setupEntranceAnimations() {
        binding.background.alpha = ZERO_ALPHA
        binding.background.animate().alpha(NORMAL_ALPHA).setDuration(DURATION_800).start()

        animateViewEntrance(binding.itindrImageText, DELAY_200)
        animateViewEntrance(binding.text, DELAY_350)
        animateViewEntrance(binding.signInButton, DELAY_500, isFromBottom = true)
        animateViewEntrance(binding.signUpButton, DELAY_650, isFromBottom = true) {
            binding.root.isHapticFeedbackEnabled = true

            @Suppress("DEPRECATION")
            binding.root.performHapticFeedback(
                android.view.HapticFeedbackConstants.VIRTUAL_KEY,
                android.view.HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
        }
    }

    private fun animateViewEntrance(
        view: View,
        delay: Long,
        isFromBottom: Boolean = false,
        onEnd: (() -> Unit)? = null
    ) {
        view.alpha = ZERO_ALPHA
        view.translationY = if (isFromBottom) TRANSLATION_Y else TRANSLATION_Y_NEGATIVE
        view.animate()
            .alpha(NORMAL_ALPHA)
            .translationY(ZERO_TRANSLATION_Y)
            .setStartDelay(delay)
            .setDuration(DURATION_700)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                onEnd?.invoke()
            }
            .start()
    }

    private fun setListeners() {
        Effects.setPressEffect(binding.signUpButton) {
            findNavController().navigate(R.id.action_initialFragment_to_signUpFragment)
        }

        Effects.setPressEffect(binding.signInButton) {
            findNavController().navigate(R.id.action_initialFragment_to_signInFragment)
        }
    }

    private fun startHeartbeatAnimation() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, NORMAL_SCALE, BIG_SCALE,
            NORMAL_SCALE)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, NORMAL_SCALE, BIG_SCALE,
            NORMAL_SCALE)

        val beat = ObjectAnimator.ofPropertyValuesHolder(binding.heart, scaleX, scaleY).apply {
            duration = DURATION_500
            interpolator = AccelerateDecelerateInterpolator()
        }

        heartAnimatorSet = AnimatorSet().apply {
            play(beat)
            startDelay = DELAY_2000
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (_binding != null) start()
                }
            })
            start()
        }
    }

    override fun onDestroyView() {
        heartAnimatorSet?.removeAllListeners()
        heartAnimatorSet?.cancel()
        _binding?.apply {
            heart.animate().cancel()
            signInButton.animate().cancel()
            signUpButton.animate().cancel()
            itindrImageText.animate().cancel()
            text.animate().cancel()
            background.animate().cancel()
        }
        super.onDestroyView()
        _binding = null
    }
}
