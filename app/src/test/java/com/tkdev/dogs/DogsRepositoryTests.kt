package com.tkdev.dogs

import com.tkdev.dogs.common.ConnectionManager
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.model.ApiResponse
import com.tkdev.dogs.model.BreedDomain
import com.tkdev.dogs.model.DogDomain
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.repository.*
import com.tkdev.dogs.repository.remote.DogsRemoteRepoMapper
import com.tkdev.dogs.repository.remote.RemoteRepository
import com.tkdev.dogs.utils.*
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DogsRepositoryTests {

    @MockK
    private lateinit var remoteRepository: RemoteRepository

    @MockK
    private lateinit var localRepository: LocalRepository

    @MockK
    private lateinit var apiMapper: DogsRemoteRepoMapper

    @MockK
    private lateinit var connectionManager: ConnectionManager

    @MockK
    private lateinit var stringWrapper: StringWrapper

    @InjectMockKs
    private lateinit var dogsRepository: DogsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }


    @Test
    fun `GIVEN no internet connection AND empty local list, WHEN getDogsList, THEN return response fail with empty list`() {
        //GIVEN
        val isConnected = false
        val localRepositoryMessage = "nothing, empty, nada"
        val localRepositoryResponse = ApiResponse.Fail<List<DogModel>>(localRepositoryMessage)
        val message = "you shall not pass!"
        val expected = ApiResponse.Fail<List<DogModel>>(message)

        every { connectionManager.checkConnection() } returns isConnected
        every { localRepository.getStoredDogsList() } returns localRepositoryResponse
        every { stringWrapper.getString(R.string.exception_no_internet) } returns message

        //WHEN
        val result = runBlocking { dogsRepository.getDogsList() }

        //THEN
        assertEquals(expected.message, result.message)
        assertEquals(expected.data, result.data)

    }

    @Test
    fun `GIVEN no internet connection AND local list, WHEN getDogsList, THEN return response fail with list`() {
        //GIVEN
        val isConnected = false
        val message = "you shall not pass!"
        val localRepositoryData = testListDogModel
        val localRepositoryResponse = ApiResponse.Success(localRepositoryData)
        val expected = ApiResponse.Fail(message, localRepositoryData)

        every { connectionManager.checkConnection() } returns isConnected
        every { localRepository.getStoredDogsList() } returns localRepositoryResponse
        every { stringWrapper.getString(R.string.exception_no_internet) } returns message

        //WHEN
        val result = runBlocking { dogsRepository.getDogsList() }

        //THEN
        assertEquals(expected.message, result.message)
        assertEquals(expected.data, result.data)

    }

    @Test
    fun `GIVEN exception, WHEN getDogsList, THEN return response fail`() {
        //GIVEN
        val isConnected = true
        val message = "you shall not pass!"
        val exception = Exception()
        val expected = ApiResponse.Fail<List<DogModel>>(message)

        every { connectionManager.checkConnection() } returns isConnected
        every { stringWrapper.getString(R.string.exception_fetch_fail) } returns message
        coEvery { remoteRepository.fetchDogsList() } throws exception

        //WHEN
        val result = runBlocking { dogsRepository.getDogsList() }

        //THEN
        assertEquals(expected.message, result.message)

    }

    @Test
    fun `GIVEN successful fetch, WHEN getDogsList, THEN return response success`() {
        //GIVEN
        val isConnected = true
        val dogDomain : DogDomain = mockk(relaxed = true)
        val expected = ApiResponse.Success(testListDogModel)

        every { connectionManager.checkConnection() } returns isConnected
        coEvery { remoteRepository.fetchDogsList() } returns dogDomain
        every { apiMapper.mapDogsList(dogDomain.message) } returns expected

        //WHEN
        val result = runBlocking { dogsRepository.getDogsList() }

        //THEN
        assertEquals(expected.data, result.data)

    }

    @Test
    fun `GIVEN no internet connection, WHEN getBreedDogPictures, THEN return response fail`() {
        //GIVEN
        val isConnected = false
        val message = "you shall not pass!"
        val expected = ApiResponse.Fail<List<DogModel>>(message)

        every { connectionManager.checkConnection() } returns isConnected
        every { stringWrapper.getString(R.string.exception_no_internet_pictures_download) } returns message

        //WHEN
        val result = runBlocking { dogsRepository.getBreedDogPictures(testDogModel) }

        //THEN
        assertEquals(expected.message, result.message)
    }

    @Test
    fun `GIVEN exception, WHEN getBreedDogPictures, THEN return response fail`() {
        //GIVEN
        val isConnected = true
        val message = "you shall not pass!"
        val exception = Exception()
        val expected = ApiResponse.Fail<List<DogModel>>(message)

        every { connectionManager.checkConnection() } returns isConnected
        every { stringWrapper.getString(R.string.exception_fetch_fail) } returns message
        coEvery { remoteRepository.fetchBreedDogPictures(testDogModel.dogBreed) } throws exception

        //WHEN
        val result = runBlocking { dogsRepository.getBreedDogPictures(testDogModel) }

        //THEN
        assertEquals(expected.message, result.message)

    }

    @Test
    fun `GIVEN successful fetch, WHEN getBreedDogPictures, THEN return response success`() {
        //GIVEN
        val isConnected = true
        val breedDomain : BreedDomain = mockk(relaxed = true)
        val expected = ApiResponse.Success(testListPictureUrl)

        every { connectionManager.checkConnection() } returns isConnected
        coEvery { remoteRepository.fetchBreedDogPictures(testDogModel.dogBreed) } returns breedDomain
        every { apiMapper.mapPicturesWithBreed(breedDomain,testDogModel) } returns expected

        //WHEN
        val result = runBlocking { dogsRepository.getBreedDogPictures(testDogModel) }

        //THEN
        assertEquals(expected.data, result.data)

    }



}