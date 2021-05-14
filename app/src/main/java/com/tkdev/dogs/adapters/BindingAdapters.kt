package com.tkdev.dogs.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("setVisibility")
fun setVisibility(view: View, enabled: Boolean) {
    when (enabled) {
        true -> view.visibility = View.VISIBLE
        false -> view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("loadImageUrl", "override")
fun loadImageUrl(image: ImageView, url: String, override: Boolean) {
    val glide = Glide.with(image.context)
    when (override) {
        true -> glide.load(url).override(300, 200).into(image)
        false -> glide.load(url).into(image)
    }
}
