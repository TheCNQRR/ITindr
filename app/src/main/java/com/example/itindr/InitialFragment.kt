package com.example.itindr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.itindr.databinding.FragmentInitialBinding

class InitialFragment : Fragment() {
    private var _binding: FragmentInitialBinding? = null
    private val binding get() = _binding!!

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

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_initialFragment_to_signUpFragment)
        }

        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_initialFragment_to_signInFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
