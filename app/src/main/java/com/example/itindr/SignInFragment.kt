package com.example.itindr

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.itindr.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            buttonEffect(it) {
                startActivity(Intent(requireContext(), MainScreenActivity::class.java))
                requireActivity().finish()
            }
        }

        binding.back.setOnClickListener {
            buttonEffect(it) {
                findNavController().navigateUp()
            }
        }

        setupTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentFocus = requireActivity().currentFocus
                if (currentFocus is EditText) {
                    hideKeyboardAndClearFocus(currentFocus)
                }
            }
            false
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

    private fun hideKeyboardAndClearFocus(currentFocus: EditText) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)

        currentFocus.clearFocus()

        binding.root.requestFocus()

        binding.yourEmail.clearFocus()
        binding.yourPassword.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
