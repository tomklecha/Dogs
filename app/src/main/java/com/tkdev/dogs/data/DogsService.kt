package com.tkdev.dogs.data

import com.tkdev.dogs.data.model.BreedDomain
import com.tkdev.dogs.data.model.DogDomain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsService {
    @GET("breeds/list/all")
    fun fetchAllDogs(): Call<DogDomain>

    @GET("breed/{breedName}/images")
    fun fetchBreedImages (@Path("breedName") breedName: String): Call<BreedDomain>
}