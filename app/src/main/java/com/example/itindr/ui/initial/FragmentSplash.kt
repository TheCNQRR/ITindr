<<<<<<<< HEAD:app/src/main/java/com/example/itindr/ui/initial/FragmentSplash.kt
package com.example.itindr.ui.initial
========
package com.example.itindr.ui
>>>>>>>> feature/custom_view_tags:app/src/main/java/com/example/itindr/ui/FragmentSplash.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.itindr.R
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

        findNavController().navigate(R.id.action_fragmentSplash_to_initialFragment)
    }
}
