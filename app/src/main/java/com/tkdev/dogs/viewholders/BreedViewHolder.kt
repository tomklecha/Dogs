package com.tkdev.dogs.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.tkdev.dogs.databinding.ItemBreedPictureBinding
import com.tkdev.dogs.views.BreedFragment

class BreedViewHolder(
    private val binding: ItemBreedPictureBinding,
    private val listener: BreedFragment.Callback?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(imageUrl: String) {
        binding.clickListener = listener
        binding.apply {
            this.url = imageUrl
            executePendingBindings()
        }
    }
}