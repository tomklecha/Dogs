package com.tkdev.dogs.adapters

import androidx.recyclerview.widget.DiffUtil
import com.tkdev.dogs.data.model.DogModel

class BreedDiffUtil : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}