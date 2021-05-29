package com.tkdev.dogs.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogModel(
    val id: Int,
    val dogBreed: String,
    val dogSubBreed: String,
    val dogDisplayName: String
) : Parcelable