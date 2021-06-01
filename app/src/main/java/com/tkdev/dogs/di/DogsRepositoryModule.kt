package com.tkdev.dogs.di

import com.tkdev.dogs.repository.DogsRepository
import com.tkdev.dogs.repository.DogsRepositoryDefault
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DogsRepositoryModule {

    @Singleton
    @Binds
    abstract fun providesDogsRepository(impl: DogsRepositoryDefault): DogsRepository
}