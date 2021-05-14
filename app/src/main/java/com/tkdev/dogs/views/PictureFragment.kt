package com.tkdev.dogs.views

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tkdev.dogs.R
import com.tkdev.dogs.databinding.DialogPictureBinding

const val IMAGE_URL = "image_url"

class PictureFragment : DialogFragment() {

    interface Callback {
        fun dismissPicture()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val imageUrl = arguments?.getString(IMAGE_URL, "")

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogPictureBinding.inflate(inflater).apply {
                callback = object : PictureFragment.Callback {
                    override fun dismissPicture() {
                        dismiss()
                    }
                }
                url = imageUrl
            }
            builder.background = ColorDrawable(android.graphics.Color.TRANSPARENT)
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException(getString(R.string.activity_exception))
    }
}