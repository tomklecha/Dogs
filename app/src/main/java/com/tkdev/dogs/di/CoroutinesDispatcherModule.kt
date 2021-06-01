package com.tkdev.dogs.di

import com.tkdev.dogs.common.CommonCoroutineDispatcher
import com.tkdev.dogs.common.CommonCoroutineDispatcherDefault
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CoroutinesModule {

    @Singleton
    @Binds
    abstract fun providesCoroutinesDispatcher(impl: CommonCoroutineDispatcherDefault): CommonCoroutineDispatcher
}