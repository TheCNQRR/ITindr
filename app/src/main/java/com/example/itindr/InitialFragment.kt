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

class InitialFragment : Fragment() {
    private var _binding: FragmentInitialBinding? = null
    private val binding get() = _binding!!

    private var heartAnimatorSet: AnimatorSet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        binding.background.alpha = 0f
        binding.background.animate().alpha(1f).setDuration(800).start()
        binding.heart.alpha = 0f
        binding.heart.animate().alpha(1f).setStartDelay(800).setDuration(800).start()

        animateViewEntrance(binding.itindrImageText, 200)
        animateViewEntrance(binding.text, 350)
        animateViewEntrance(binding.signInButton, 500, isFromBottom = true)
        animateViewEntrance(binding.signUpButton, 650, isFromBottom = true)
    }

    private fun animateViewEntrance(view: View, delay: Long, isFromBottom: Boolean = false) {
        view.alpha = 0f
        view.translationY = if (isFromBottom) 200f else -200f
        view.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(delay)
            .setDuration(700)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun setListeners() {
        binding.signUpButton.setOnClickListener {
            buttonEffect(it) {
                findNavController().navigate(R.id.action_initialFragment_to_signUpFragment)
            }
        }
        binding.signInButton.setOnClickListener {
            buttonEffect(it) {
                findNavController().navigate(R.id.action_initialFragment_to_signInFragment)
            }
        }
    }

    private fun buttonEffect(view: View, onComplete: () -> Unit) {
        view.apply {
            animate().cancel()
            animate().setStartDelay(0)

            isClickable = false

            animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .alpha(0.5f)
                .setDuration(300)
                .withEndAction {
                    animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(400)
                        .withEndAction {
                            isClickable = true
                        }
                        .start()

                    onComplete()
                }
                .start()
        }
    }

    private fun startHeartbeatAnimation() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f, 1f)

        val beat = ObjectAnimator.ofPropertyValuesHolder(binding.heart, scaleX, scaleY).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        heartAnimatorSet = AnimatorSet().apply {
            play(beat)
            startDelay = 2000
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