package com.example.itindr.ui

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
import com.example.itindr.R
import com.example.itindr.databinding.FragmentAboutMeBinding
import com.example.itindr.ui.customView.TagListView
import com.example.itindr.util.dp
import com.example.itindr.util.setPressEffect

private const val TRANSLATION_OFFSET_NEGATIVE_VALUE = -20
private const val TRANSLATION_OFFSET_VALUE = 20
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

        setPressEffect(binding.save) {
            startActivity(Intent(requireContext(), MainScreenActivity::class.java))
            requireActivity().finish()
        }

        setupTouchListener()
        setupPhotoButtonsListeners()

        val tagListView = binding.tagListView
        tagListView.setTags(
            listOf(
                TagListView.Tag(getString(R.string.python), getString(R.string.python)),
                TagListView.Tag(getString(R.string.django), getString(R.string.django)),
                TagListView.Tag(getString(R.string.rest), getString(R.string.rest)),
                TagListView.Tag(getString(R.string.swift), getString(R.string.swift)),
                TagListView.Tag(getString(R.string.obj_c), getString(R.string.obj_c)),
                TagListView.Tag(getString(R.string.react_js), getString(R.string.react_js)),
                TagListView.Tag(getString(R.string.kotlin), getString(R.string.kotlin)),
                TagListView.Tag(getString(R.string.git), getString(R.string.git)),
                TagListView.Tag(getString(R.string.unity), getString(R.string.unity)),
                TagListView.Tag(getString(R.string.net), getString(R.string.net)),
                TagListView.Tag(getString(R.string.sql), getString(R.string.sql)),
                TagListView.Tag(getString(R.string.clean_architecture), getString(R.string.clean_architecture)),
                TagListView.Tag(getString(R.string.uml), getString(R.string.uml))
            )
        )
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
        setPressEffect(binding.choosePhoto) {
            setupAddPhotoButtonListener()
        }

        setPressEffect(binding.galleryPicture) {
            setupAddPhotoButtonListener()
        }

        setPressEffect(binding.deletePhoto) {
            setupDeletePhotoButtonListener()
        }

        setPressEffect(binding.trashPicture) {
            setupDeletePhotoButtonListener()
        }
    }

    private fun setupAddPhotoButtonListener() {
        val translationOffsetNegative = TRANSLATION_OFFSET_NEGATIVE_VALUE.dp.toFloat()
        val translationOffset = TRANSLATION_OFFSET_VALUE.dp.toFloat()

        binding.choosePhoto.animate()
            .alpha(0f)
            .setDuration(DURATION_50)
            .start()

        binding.galleryPicture.animate()
            .alpha(0f)
            .setDuration(DURATION_200)
            .start()

        binding.choosePhoto.visibility = View.INVISIBLE
        binding.galleryPicture.visibility = View.INVISIBLE

        binding.changePhoto.visibility = View.VISIBLE
        binding.changePhoto.alpha = 0f

        binding.changePhoto.animate()
            .translationY(translationOffset)
            .alpha(1f)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()

        binding.galleryPicture1.visibility = View.VISIBLE
        binding.galleryPicture1.alpha = 0f

        binding.galleryPicture1.animate()
            .translationY(translationOffset)
            .alpha(1f)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()

        binding.deletePhoto.visibility = View.VISIBLE
        binding.deletePhoto.alpha = 0f

        binding.deletePhoto.animate()
            .translationY(translationOffsetNegative)
            .alpha(1f)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()

        binding.trashPicture.visibility = View.VISIBLE
        binding.trashPicture.alpha = 0f

        binding.trashPicture.animate()
            .translationY(translationOffsetNegative)
            .alpha(1f)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()
    }

    private fun setupDeletePhotoButtonListener() {
        val translationOffsetNegative = TRANSLATION_OFFSET_NEGATIVE_VALUE.dp.toFloat()

        val translationOffset = TRANSLATION_OFFSET_VALUE.dp.toFloat()

        binding.deletePhoto.animate()
            .translationY(translationOffset)
            .alpha(0f)
            .setDuration(DURATION_50)
            .start()
        binding.deletePhoto.visibility = View.INVISIBLE

        binding.trashPicture.animate()
            .translationY(translationOffset)
            .alpha(0f)
            .setDuration(DURATION_50)
            .start()
        binding.trashPicture.visibility = View.INVISIBLE

        binding.changePhoto.animate()
            .translationY(translationOffsetNegative)
            .alpha(0f)
            .setDuration(DURATION_50)
            .start()

        binding.changePhoto.visibility = View.INVISIBLE

        binding.galleryPicture1.animate()
            .translationY(translationOffsetNegative)
            .alpha(0f)
            .setDuration(DURATION_50)
            .start()

        binding.galleryPicture1.visibility = View.INVISIBLE

        binding.choosePhoto.visibility = View.VISIBLE
        binding.choosePhoto.alpha = 0f

        binding.galleryPicture.visibility = View.VISIBLE
        binding.galleryPicture.alpha = 0f

        binding.choosePhoto.animate()
            .alpha(1f)
            .setDuration(DURATION_200)
            .setStartDelay(DURATION_50)
            .start()

        binding.galleryPicture.animate()
            .alpha(1f)
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
