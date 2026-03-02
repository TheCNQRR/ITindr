package com.example.itindr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.itindr.databinding.FragmentSplashBinding

private const val ZERO_ALPHA = 0f
private const val NORMAL_ALPHA = 1f
private const val SCALE = 2f
private const val DURATION_300 = 300L
private const val DURATION_400 = 400L
private const val ZERO_TRANSLATION = 0f
private const val TRANSLATION_50 = 50f
private const val TRANSLATION_278 = (-278f)

class FragmentSplash : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.alpha = ZERO_ALPHA
        binding.name.translationX = TRANSLATION_50

        animateLogo()
    }

    private fun animateLogo() {
        binding.logo.alpha = NORMAL_ALPHA

        binding.logo.animate()
            .scaleX(SCALE)
            .scaleY(SCALE)
            .setDuration(DURATION_300)
            .withEndAction {
                binding.logo.animate()
                    .translationX(TRANSLATION_278)
                    .setDuration(DURATION_400)
            }

        binding.name.animate()
            .alpha(NORMAL_ALPHA)
            .translationX(ZERO_TRANSLATION)
            .setStartDelay(DURATION_300)
            .setDuration(DURATION_400)
            .withEndAction {
                view?.postDelayed({
                    if (_binding != null) {
                        findNavController().navigate(R.id.action_fragmentSplash_to_initialFragment)
                    }
                }, DURATION_300)
            }
    }
}
