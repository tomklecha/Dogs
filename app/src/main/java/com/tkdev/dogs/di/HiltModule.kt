package com.tkdev.dogs.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.tkdev.dogs.common.CommonCoroutineDispatcher
import com.tkdev.dogs.common.CommonCoroutineDispatcherDefault
import com.tkdev.dogs.common.ConnectionManager
import com.tkdev.dogs.common.ConnectionManagerDefault
import com.tkdev.dogs.repository.DogsRepository
import com.tkdev.dogs.repository.DogsRepositoryDefault
import com.tkdev.dogs.repository.local.LocalRepository
import com.tkdev.dogs.repository.local.LocalRepositoryDefault
import com.tkdev.dogs.repository.remote.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val SHARED_PREF_NAME = "shared_preferences"

@InstallIn(SingletonComponent::class)
@Module
abstract class DogsRepositoryModule {

    @Singleton
    @Binds
    abstract fun providesDogsRepository(impl: DogsRepositoryDefault): DogsRepository
}

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteRepoModule {

    @Singleton
    @Binds
    abstract fun providesRemoteRepo(impl: RemoteRepositoryDefault): RemoteRepository

}

@InstallIn(SingletonComponent::class)
@Module
abstract class CoroutinesModule {

    @Singleton
    @Binds
    abstract fun providesCoroutinesDispatcher(impl: CommonCoroutineDispatcherDefault): CommonCoroutineDispatcher
}

@InstallIn(SingletonComponent::class)
@Module
abstract class LocalRepoModule {

    @Singleton
    @Binds
    abstract fun providesLocalRepo(impl: LocalRepositoryDefault): LocalRepository

}

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteRepoMapperModule {

    @Singleton
    @Binds
    abstract fun providesRemoteApiMapper(impl: DogsRemoteRepoMapperDefault): DogsRemoteRepoMapper

}

@InstallIn(SingletonComponent::class)
@Module
abstract class ConnectionManagerModule {

    @Singleton
    @Binds
    abstract fun providesConnectManager(impl: ConnectionManagerDefault): ConnectionManager

}

@InstallIn(SingletonComponent::class)
@Module
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun providesSharedPref(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

}

@InstallIn(SingletonComponent::class)
@Module
object ConnectivityManagerModule {

    @Singleton
    @Provides
    fun providesConnectManagerObject(@ApplicationContext appContext: Context): ConnectivityManager {
        return appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

}

@InstallIn(SingletonComponent::class)
@Module
object DogsServiceModule {

    @Singleton
    @Provides
    fun providesDogsServiceModule(): DogsService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(DogsService::class.java)
    }

}



