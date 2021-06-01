package com.tkdev.dogs.repository.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tkdev.dogs.R
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.model.ApiResponse
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.repository.DOGS_LIST
import javax.inject.Inject
import javax.inject.Singleton

interface LocalRepository {
    fun getStoredDogsList(): ApiResponse<List<DogModel>>
    fun storeDogsList(list: List<DogModel>)
}

@Singleton
class LocalRepositoryDefault @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val stringWrapper: StringWrapper
) : LocalRepository {

    override fun getStoredDogsList(): ApiResponse<List<DogModel>> {
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

    override fun storeDogsList(list: List<DogModel>) {
        sharedPreferences.edit().apply {
            val gson = Gson()
            val json = gson.toJson(list)
            putString(DOGS_LIST, json).apply()
        }
    }
}