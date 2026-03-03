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
import com.example.itindr.databinding.FragmentAboutMeBinding
import com.example.itindr.util.Effects
import com.example.itindr.util.Functions.dp

private const val ZERO = 0
private const val TRANSLATION_OFFSET_NEGATIVE_VALUE = -20
private const val TRANSLATION_OFFSET_VALUE = 20
private const val ZERO_ALPHA = 0f
private const val ALPHA = 1f
private const val DURATION_50 = 50L
private const val DURATION_200 = 200L

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutYourselfField.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.parent.requestDisallowInterceptTouchEvent(true)
            }
            false
        }

        binding.aboutYourselfField.movementMethod = android.text.method.ScrollingMovementMethod.getInstance()

        Effects.setPressEffect(binding.save) {
            startActivity(Intent(requireContext(), MainScreenActivity::class.java))
            requireActivity().finish()
        }

        setupInterestsClickListeners()
        setupTouchListener()
        setupPhotoButtonsListeners()
    }

    private fun setupInterestsClickListeners() {
        for (i in ZERO until binding.chipGroup.childCount) {
            binding.chipGroup.getChildAt(i).setOnClickListener {
                it.isSelected = !it.isSelected
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        binding.scrollView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentFocus = requireActivity().currentFocus
                if (currentFocus is EditText) {
                    hideKeyboardAndClearFocus(currentFocus)
                }
            }
            false
        }
    }

    private fun setupPhotoButtonsListeners() {
        Effects.setPressEffect(binding.choosePhoto) {
            setupAddPhotoButtonListener()
        }

        Effects.setPressEffect(binding.galleryPicture) {
            setupAddPhotoButtonListener()
        }

        Effects.setPressEffect(binding.deletePhoto) {
            setupDeletePhotoButtonListener()
        }

        Effects.setPressEffect(binding.trashPicture) {
            setupDeletePhotoButtonListener()
        }
    }

    private fun setupAddPhotoButtonListener() {
        val translationOffsetNegative = TRANSLATION_OFFSET_NEGATIVE_VALUE.dp.toFloat()
        val translationOffset = TRANSLATION_OFFSET_VALUE.dp.toFloat()

        binding.choosePhoto.animate()
            .alpha(ZERO_ALPHA)
            .setDuration(DURATION_50)
            .start()

        binding.galleryPicture.animate()
            .alpha(ZERO_ALPHA)
            .setDuration(DURATION_200)
            .start()

        binding.choosePhoto.visibility = View.INVISIBLE
        binding.galleryPicture.visibility = View.INVISIBLE

        binding.changePhoto.visibility = View.VISIBLE
        binding.changePhoto.alpha = ZERO_ALPHA

        binding.changePhoto.animate()
            .translationY(translationOffset)
            .alpha(ALPHA)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()

        binding.galleryPicture1.visibility = View.VISIBLE
        binding.galleryPicture1.alpha = ZERO_ALPHA

        binding.galleryPicture1.animate()
            .translationY(translationOffset)
            .alpha(ALPHA)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()

        binding.deletePhoto.visibility = View.VISIBLE
        binding.deletePhoto.alpha = ZERO_ALPHA

        binding.deletePhoto.animate()
            .translationY(translationOffsetNegative)
            .alpha(ALPHA)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()

        binding.trashPicture.visibility = View.VISIBLE
        binding.trashPicture.alpha = ZERO_ALPHA

        binding.trashPicture.animate()
            .translationY(translationOffsetNegative)
            .alpha(ALPHA)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()
    }

    private fun setupDeletePhotoButtonListener() {
        val translationOffsetNegative = TRANSLATION_OFFSET_NEGATIVE_VALUE.dp.toFloat()

        val translationOffset = TRANSLATION_OFFSET_VALUE.dp.toFloat()

        binding.deletePhoto.animate()
            .translationY(translationOffset)
            .alpha(ZERO_ALPHA)
            .setDuration(DURATION_50)
            .start()
        binding.deletePhoto.visibility = View.INVISIBLE

        binding.trashPicture.animate()
            .translationY(translationOffset)
            .alpha(ZERO_ALPHA)
            .setDuration(DURATION_50)
            .start()
        binding.trashPicture.visibility = View.INVISIBLE

        binding.changePhoto.animate()
            .translationY(translationOffsetNegative)
            .alpha(ZERO_ALPHA)
            .setDuration(DURATION_50)
            .start()

        binding.changePhoto.visibility = View.INVISIBLE

        binding.galleryPicture1.animate()
            .translationY(translationOffsetNegative)
            .alpha(ZERO_ALPHA)
            .setDuration(DURATION_50)
            .start()

        binding.galleryPicture1.visibility = View.INVISIBLE

        binding.choosePhoto.visibility = View.VISIBLE
        binding.choosePhoto.alpha = ZERO_ALPHA

        binding.galleryPicture.visibility = View.VISIBLE
        binding.galleryPicture.alpha = ZERO_ALPHA

        binding.choosePhoto.animate()
            .alpha(ALPHA)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()

        binding.galleryPicture.animate()
            .alpha(ALPHA)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()
    }

    private fun hideKeyboardAndClearFocus(currentFocus: EditText) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)

        currentFocus.clearFocus()

        binding.scrollView.requestFocus()

        binding.yourName.clearFocus()
        binding.aboutYourselfField.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
