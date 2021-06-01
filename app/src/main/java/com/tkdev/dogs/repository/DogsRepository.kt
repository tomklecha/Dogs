package com.tkdev.dogs.repository


import androidx.annotation.StringRes
import com.tkdev.dogs.R
import com.tkdev.dogs.common.ConnectionManager
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.model.ApiResponse
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.repository.local.LocalRepository
import com.tkdev.dogs.repository.remote.DogsRemoteRepoMapper
import com.tkdev.dogs.repository.remote.RemoteRepository
import javax.inject.Inject
import javax.inject.Singleton

const val DOGS_LIST = "dogs_list"

interface DogsRepository {
    suspend fun getDogsList(): ApiResponse<List<DogModel>>
    suspend fun getBreedDogPictures(dogModel: DogModel): ApiResponse<List<String>>
}

@Singleton
class DogsRepositoryDefault @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val mapper: DogsRemoteRepoMapper,
    private val connectionManager: ConnectionManager,
    private val stringWrapper: StringWrapper
) : DogsRepository {

    override suspend fun getDogsList(): ApiResponse<List<DogModel>> =

        when (connectionManager.checkConnection()) {
            true -> try {
                val dogRemoteDomain = remoteRepository.fetchDogsList().message
                when (val remoteResponse = mapper.mapDogsList(dogRemoteDomain)) {
                    is ApiResponse.Success -> {
                        remoteResponse.data?.let { localRepository.storeDogsList(it) }
                        remoteResponse
                    }
                    else -> {
                        localRepository.getStoredDogsList()
                    }
                }
            } catch (e: Exception) {
                ApiResponse.Fail(stringWrapper.getString(R.string.exception_fetch_fail))
            }
            false -> {
                ApiResponse.Fail(
                    stringWrapper.getString(R.string.exception_no_internet),
                    localRepository.getStoredDogsList().data
                )
            }
        }

    override suspend fun getBreedDogPictures(dogModel: DogModel): ApiResponse<List<String>> {

        return when (connectionManager.checkConnection()) {
            true -> try {
                val breedDomain = remoteRepository.fetchBreedDogPictures(dogModel.dogBreed)
                mapper.mapPicturesWithBreed(breedDomain, dogModel)
            } catch (e: Exception) {
                ApiResponse.Fail(stringWrapper.getString(R.string.exception_fetch_fail))
            }
            false -> {
                ApiResponse.Fail(stringWrapper.getString(R.string.exception_no_internet_pictures_download))
            }
        }
    }

}