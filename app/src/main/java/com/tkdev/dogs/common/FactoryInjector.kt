package com.tkdev.dogs.common

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.tkdev.dogs.repository.remote.DogsRemoteRepoMapper
import com.tkdev.dogs.repository.remote.DogsRemoteRepoMapperDefault
import com.tkdev.dogs.repository.*
import com.tkdev.dogs.repository.remote.RemoteRepository
import com.tkdev.dogs.repository.remote.RemoteRepositoryDefault
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
        val remoteRepository = getRemoteRepository()
        val sharedPreferences = getSharedPreferences(context)
        val stringWrapper = getStringWrapper(context)
        val localRepository = getLocalRepository(sharedPreferences, stringWrapper)
        val mapper = getDogsApiMapper(stringWrapper)
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectionManager = getConnectionManager(connectivityManager)
        return DogsRepository(remoteRepository, localRepository, mapper, connectionManager,  stringWrapper)
    }

    private fun getCommonCoroutineDispatcher(): CommonCoroutineDispatcher = CommonCoroutineDispatcherDefault()

    private fun getConnectionManager(connectivityManager: ConnectivityManager): ConnectionManager = ConnectionManagerDefault(connectivityManager)

    private fun getStringWrapper(context: Context): StringWrapper = StringWrapperDefault(context)

    private fun getSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private fun getDogsApiMapper(stringWrapper: StringWrapper): DogsRemoteRepoMapper = DogsRemoteRepoMapperDefault(stringWrapper)

    private fun getRemoteRepository() : RemoteRepository = RemoteRepositoryDefault()

    private fun getLocalRepository(
        sharedPreferences: SharedPreferences,
        stringWrapper: StringWrapper
    ): LocalRepository = LocalRepositoryDefault(sharedPreferences, stringWrapper)
}