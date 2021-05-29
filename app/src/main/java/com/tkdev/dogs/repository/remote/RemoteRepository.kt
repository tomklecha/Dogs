package com.tkdev.dogs.repository.remote

import com.tkdev.dogs.model.BreedDomain
import com.tkdev.dogs.model.DogDomain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.await

import retrofit2.converter.gson.GsonConverterFactory

interface RemoteRepository {
    suspend fun fetchDogsList(): DogDomain
    suspend fun fetchBreedDogPictures(breed: String): BreedDomain
}

private const val BASE_URL = "https://dog.ceo/api/"

class RemoteRepositoryDefault : RemoteRepository {

    private var service: DogsService

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        service = retrofit.create(DogsService::class.java)
    }

    override suspend fun fetchDogsList(): DogDomain = service.fetchAllDogs().await()

    override suspend fun fetchBreedDogPictures(breed: String): BreedDomain =
        service.fetchBreedImages(breed).await()
}