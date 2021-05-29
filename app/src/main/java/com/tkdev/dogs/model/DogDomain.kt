package com.tkdev.dogs.model

data class DogDomain(
    val message: Map<String, List<String>>,
    val status: String
)