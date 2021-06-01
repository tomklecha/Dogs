package com.tkdev.dogs.di

import com.tkdev.dogs.common.ConnectionManager
import com.tkdev.dogs.common.ConnectionManagerDefault
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.common.StringWrapperDefault
import com.tkdev.dogs.repository.local.LocalRepository
import com.tkdev.dogs.repository.local.LocalRepositoryDefault
import com.tkdev.dogs.repository.remote.DogsRemoteRepoMapper
import com.tkdev.dogs.repository.remote.DogsRemoteRepoMapperDefault
import com.tkdev.dogs.repository.remote.RemoteRepository
import com.tkdev.dogs.repository.remote.RemoteRepositoryDefault
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteRepoModule {

    @Singleton
    @Binds
    abstract fun providesRemoteRepo(impl: RemoteRepositoryDefault): RemoteRepository

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
abstract class StringWrapperModule {

    @Singleton
    @Binds
    abstract fun providesStringWrapper(impl: StringWrapperDefault): StringWrapper

}






