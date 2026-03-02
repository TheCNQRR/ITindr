package com.example.itindr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.itindr.databinding.FragmentSplashBinding

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

        binding.name.alpha = 0f
        binding.name.translationX = 50f

        animateLogo()
    }

    private fun animateLogo() {
        binding.logo.alpha = 1f

        binding.logo.animate()
            .scaleX(2f)
            .scaleY(2f)
            .setDuration(300)
            .withEndAction {
                binding.logo.animate()
                    .translationX((-278).toFloat())
                    .setDuration(400)
            }

        binding.name.animate()
            .alpha(1f)
            .translationX(0f)
            .setStartDelay(300)
            .setDuration(400)
            .withEndAction {
                view?.postDelayed({
                    if (_binding != null) {
                        findNavController().navigate(R.id.action_fragmentSplash_to_initialFragment)
                    }
                }, 300)
            }
    }
}