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
private const val SCALE_SMALL = 0.1f
private const val SCALE = 2f
private const val DURATION_300 = 300L
private const val DURATION_400 = 400L
private const val TRANSLATION_50 = 50f
private const val MARGIN_16 = 16

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

        findNavController().navigate(R.id.action_fragmentSplash_to_initialFragment)

        binding.name.alpha = ZERO_ALPHA
        binding.name.translationX = TRANSLATION_50
        binding.logo.scaleX = SCALE_SMALL
        binding.logo.scaleY = SCALE_SMALL

        animateLogo()
    }

    private fun animateLogo() {
        binding.root.post {
            if (_binding == null) {
                return@post
            }

            val nameWidth = binding.name.width.toFloat()
            val margin = MARGIN_16 * resources.displayMetrics.density

            val shiftValue = -(nameWidth + margin) / SCALE

            binding.logo.animate()
                .scaleX(SCALE)
                .scaleY(SCALE)
                .setDuration(DURATION_300)
                .withEndAction {
                    binding.logo.animate()
                        .translationX(shiftValue)
                        .setDuration(DURATION_400)
                        .start()

                    binding.name.translationX = shiftValue + TRANSLATION_50
                    binding.name.animate()
                        .alpha(NORMAL_ALPHA)
                        .translationX(shiftValue)
                        .setDuration(DURATION_400)
                        .withEndAction {
                            binding.root.postDelayed({
                                if (_binding != null) {
                                    findNavController().navigate(R.id.action_fragmentSplash_to_initialFragment)
                                }
                            }, DURATION_300)
                        }
                        .start()
                }
                .start()
        }
    }
}
