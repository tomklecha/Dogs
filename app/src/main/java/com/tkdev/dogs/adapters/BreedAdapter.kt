package com.tkdev.dogs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tkdev.dogs.data.model.DogModel
import com.tkdev.dogs.databinding.ItemBreedPictureBinding
import com.tkdev.dogs.databinding.ItemDogListBinding
import com.tkdev.dogs.viewholders.BreedViewHolder
import com.tkdev.dogs.viewholders.DogListViewHolder
import com.tkdev.dogs.views.BreedFragment
import com.tkdev.dogs.views.DogsListFragment

class BreedAdapter(private val listener: BreedFragment.Callback?) : ListAdapter<String, BreedViewHolder>(BreedDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder =
        BreedViewHolder(
            ItemBreedPictureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )


    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}