package com.tkdev.dogs.common

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.tkdev.dogs.data.DogsApi
import com.tkdev.dogs.data.DogsApiDefault
import com.tkdev.dogs.data.DogsApiMapper
import com.tkdev.dogs.data.DogsApiMapperDefault
import com.tkdev.dogs.repository.DogsRepository
import com.tkdev.dogs.viewmodels.DogsViewModelFactory

private const val SHARED_PREF_NAME = "shared_preferences"

object FactoryInjector {

    fun provideDogsViewModelFactory(context: Context): DogsViewModelFactory {
        val repository = getDogsRepository(context)
        val coroutineDispatcher = getCommonCoroutineDispatcher()
        return DogsViewModelFactory(
            repository, coroutineDispatcher
        )
    }

    private fun getDogsRepository(context: Context) : DogsRepository {
        val api = getDogsApi()
        val stringWrapper = getStringWrapper(context)
        val mapper = getDogsApiMapper(stringWrapper)
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectionManager = getConnectionManager(connectivityManager)
        val sharedPreferences = getSharedPreferences(context)
        return DogsRepository(api, mapper, connectionManager, sharedPreferences, stringWrapper)
    }

    private fun getCommonCoroutineDispatcher(): CommonCoroutineDispatcher = CommonCoroutineDispatcherDefault()

    private fun getConnectionManager(connectivityManager: ConnectivityManager): ConnectionManager = ConnectionManagerDefault(connectivityManager)

    private fun getStringWrapper(context: Context): StringWrapper = StringWrapperDefault(context)

    private fun getSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private fun getDogsApiMapper(stringWrapper: StringWrapper): DogsApiMapper = DogsApiMapperDefault(stringWrapper)

    private fun getDogsApi() : DogsApi = DogsApiDefault()
}