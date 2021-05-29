package com.tkdev.dogs.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.databinding.ItemDogListBinding
import com.tkdev.dogs.views.DogsListFragment

class DogListViewHolder(
    private val binding: ItemDogListBinding,
    private val listener: DogsListFragment.Callback?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dogModel: DogModel) {
        binding.clickListener = listener
        binding.apply {
            this.dogModel = dogModel
            executePendingBindings()
        }
    }
}