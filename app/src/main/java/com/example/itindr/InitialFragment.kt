package com.example.itindr

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.itindr.databinding.FragmentInitialBinding

class InitialFragment : Fragment() {
    private var _binding: FragmentInitialBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private var heartbeatRunnable: Runnable? = null

    private var heartbeatAnimator: ObjectAnimator? = null
    private var secondBeatAnimator: ObjectAnimator? = null

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

        binding.background.alpha=0.1f
        binding.background.animate()
            .alpha(1f)
            .setDuration(900)
            .interpolator = AnimationUtils.loadInterpolator(context, android.R.interpolator.decelerate_quad)

        setListeners()
        animateText()
        animateButtons()
        animateHeart()
    }

    private fun animateText() {
        binding.itindrImageText.apply {
            alpha = 0f
            translationY = (-200).toFloat()
            animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(900)
                .setDuration(1500)
                .interpolator = AnimationUtils.loadInterpolator(requireContext(), android.R.interpolator.decelerate_quad)
        }

        binding.text.apply {
            alpha = 0f
            translationY = (-200).toFloat()
            animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(1100)
                .setDuration(1500)
                .interpolator = AnimationUtils.loadInterpolator(requireContext(), android.R.interpolator.decelerate_quad)
        }
    }

    private fun animateButtons() {
        binding.signInButton.apply {
            alpha = 0f
            translationY = (200).toFloat()
            animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(1400)
                .setDuration(1300)
                .interpolator = AnimationUtils.loadInterpolator(requireContext(), android.R.interpolator.decelerate_quad)
        }

        binding.signUpButton.apply {
            alpha = 0f
            translationY = (200).toFloat()
            animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(1600)
                .setDuration(1300)
                .interpolator = AnimationUtils.loadInterpolator(requireContext(), android.R.interpolator.decelerate_quad)
        }
    }

    private fun setListeners() {
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_initialFragment_to_signUpFragment)
        }

        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_initialFragment_to_signInFragment)
        }
    }

    private fun animateHeart() {
        binding.heart.apply {
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(2600)
                .setDuration(800)
                .setInterpolator(AnimationUtils.loadInterpolator(requireContext(), android.R.interpolator.decelerate_quad))
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        if (isAdded) {
                            startHeartbeatAnimation()
                        }
                    }
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .start()
        }
    }

    private fun startHeartbeatAnimation() {
        heartbeatAnimator?.cancel()
        secondBeatAnimator?.cancel()
        heartbeatRunnable?.let { handler.removeCallbacks(it) }

        val beat1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1.1f)
        val beat1Y = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1.1f)

        val beat2 = PropertyValuesHolder.ofFloat("scaleX", 1.1f, 1.2f, 1f)
        val beat2Y = PropertyValuesHolder.ofFloat("scaleY", 1.1f, 1.2f, 1f)

        val heartbeatAnimator = ObjectAnimator.ofPropertyValuesHolder(
            binding.heart,
            beat1,
            beat1Y
        ).apply {
            duration = 200
            interpolator = AnimationUtils.loadInterpolator(requireContext(), android.R.interpolator.fast_out_slow_in)
        }

        val secondBeatAnimator = ObjectAnimator.ofPropertyValuesHolder(
            binding.heart,
            beat2,
            beat2Y
        ).apply {
            duration = 150
            interpolator = AnimationUtils.loadInterpolator(requireContext(), android.R.interpolator.fast_out_slow_in)
        }

        heartbeatRunnable = object : Runnable {
            override fun run() {
                if (isAdded && _binding != null) {
                    heartbeatAnimator.start()

                    handler.postDelayed({
                        if (isAdded && _binding != null) {
                            secondBeatAnimator.start()
                        }
                    }, 250)

                    handler.postDelayed(this, 1500)
                }
            }
        }

        heartbeatRunnable?.run()
    }

    private fun stopAllAnimations() {
        heartbeatAnimator?.cancel()
        secondBeatAnimator?.cancel()
        heartbeatRunnable?.let { handler.removeCallbacks(it) }

        binding.heart.animate().cancel()
        binding.itindrImageText.animate().cancel()
        binding.text.animate().cancel()
        binding.signInButton.animate().cancel()
        binding.signUpButton.animate().cancel()
        binding.background.animate().cancel()
    }

    override fun onDestroyView() {
        stopAllAnimations()

        heartbeatAnimator = null
        secondBeatAnimator = null
        heartbeatRunnable = null

        super.onDestroyView()
        _binding = null
    }
}
