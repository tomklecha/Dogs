package com.tkdev.dogs.repository.remote

import com.tkdev.dogs.model.BreedDomain
import com.tkdev.dogs.model.DogDomain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.await

import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

interface RemoteRepository {
    suspend fun fetchDogsList(): DogDomain
    suspend fun fetchBreedDogPictures(breed: String): BreedDomain
}


@Singleton
class RemoteRepositoryDefault @Inject constructor(
    private var service: DogsService
): RemoteRepository {

    override suspend fun fetchDogsList(): DogDomain = service.fetchAllDogs().await()

    override suspend fun fetchBreedDogPictures(breed: String): BreedDomain =
        service.fetchBreedImages(breed).await()
}