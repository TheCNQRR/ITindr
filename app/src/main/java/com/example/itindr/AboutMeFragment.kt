package com.example.itindr

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.itindr.databinding.FragmentAboutMeBinding

class AboutMeFragment : Fragment() {
    private var _binding: FragmentAboutMeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutMeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.save.setOnClickListener {
            startActivity(Intent(requireContext(), MainScreenActivity::class.java))
            requireActivity().finish()
        }

        setupInterestsClickListeners()
    }

    private fun setupInterestsClickListeners() {
        val interests = listOf(
            binding.python, binding.django, binding.rest, binding.swift,
            binding.objC, binding.reactJs, binding.kotlin, binding.git,
            binding.unity, binding.net, binding.sql, binding.cleanArchitecture,
            binding.uml
        )

        interests.forEach { textView ->
            textView.setOnClickListener {
                it.isSelected = !it.isSelected
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
