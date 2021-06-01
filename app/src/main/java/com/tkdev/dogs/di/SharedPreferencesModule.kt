package com.tkdev.dogs.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SHARED_PREF_NAME = "shared_preferences"

@InstallIn(SingletonComponent::class)
@Module
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun providesSharedPref(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

}