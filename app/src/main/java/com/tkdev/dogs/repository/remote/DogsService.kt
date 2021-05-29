package com.tkdev.dogs.repository.remote

import com.tkdev.dogs.model.BreedDomain
import com.tkdev.dogs.model.DogDomain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsService {
    @GET("breeds/list/all")
    fun fetchAllDogs(): Call<DogDomain>

    @GET("breed/{breedName}/images")
    fun fetchBreedImages (@Path("breedName") breedName: String): Call<BreedDomain>
}