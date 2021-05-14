package com.tkdev.dogs.repository

import android.content.SharedPreferences
import androidx.annotation.StringRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tkdev.dogs.R
import com.tkdev.dogs.common.ConnectionManager
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.data.DogsApi
import com.tkdev.dogs.data.DogsApiMapper
import com.tkdev.dogs.data.model.ApiResponse
import com.tkdev.dogs.data.model.DogModel

const val DOGS_LIST = "dogs_list"

class DogsRepository(
    private val api: DogsApi,
    private val mapper: DogsApiMapper,
    private val connectionManager: ConnectionManager,
    private val sharedPreferences: SharedPreferences,
    private val stringWrapper: StringWrapper
) {

    fun getStringMessage(@StringRes id: Int): String = stringWrapper.getString(id)

    fun getStoredDogsList(): ApiResponse<List<DogModel>> {
        val json = sharedPreferences.getString(DOGS_LIST, null)
        return when (json != null) {
            true -> {
                val gson = Gson()
                val type = object : TypeToken<List<DogModel>>() {}.type
                ApiResponse.Success(gson.fromJson(json, type))
            }
            false -> ApiResponse.Fail(stringWrapper.getString(R.string.dogs_list_stored_empty))
        }
    }

    fun storeDogsList(list: List<DogModel>) {
        sharedPreferences.edit().apply {
            val gson = Gson()
            val json = gson.toJson(list)
            putString(DOGS_LIST, json).apply()
        }
    }

    suspend fun getDogsList(): ApiResponse<List<DogModel>> {

        return when (connectionManager.checkConnection()){
            true -> try {
                val dogDomain = api.fetchDogsList().message
                mapper.mapDogsList(dogDomain)
            } catch (e: Exception) {
                ApiResponse.Fail(stringWrapper.getString(R.string.exception_fetch_fail))
            }
            false -> {
                ApiResponse.Fail(stringWrapper.getString(R.string.exception_no_internet))
            }
        }
    }

    suspend fun getBreedDogPictures(dogModel: DogModel): ApiResponse<List<String>> {

        return when (connectionManager.checkConnection()) {
            true -> try {
                val breedDomain = api.fetchBreedDogPictures(dogModel.dogBreed)
                mapper.mapPicturesWithBreed(breedDomain, dogModel)
            } catch (e: Exception) {
                ApiResponse.Fail(stringWrapper.getString(R.string.exception_fetch_fail))
            }
            false -> {
                ApiResponse.Fail(stringWrapper.getString(R.string.exception_no_internet))
            }
        }
    }

}