package com.tkdev.dogs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tkdev.dogs.data.model.DogModel
import com.tkdev.dogs.databinding.ItemDogListBinding
import com.tkdev.dogs.viewholders.DogListViewHolder
import com.tkdev.dogs.views.DogsListFragment

class DogsListAdapter(private val listener: DogsListFragment.Callback?) : ListAdapter<DogModel, DogListViewHolder>(DogListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogListViewHolder =
      DogListViewHolder(
            ItemDogListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )

    override fun onBindViewHolder(holder: DogListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
