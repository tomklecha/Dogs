package com.tkdev.dogs.adapters

import androidx.recyclerview.widget.DiffUtil
import com.tkdev.dogs.model.DogModel

class DogListDiffUtil : DiffUtil.ItemCallback<DogModel>(){
    override fun areItemsTheSame(oldItem: DogModel, newItem: DogModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DogModel, newItem: DogModel): Boolean {
        return oldItem == newItem
    }

}