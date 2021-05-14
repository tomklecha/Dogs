package com.tkdev.dogs

import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.tkdev.dogs.common.ConnectionManager
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.data.DogsApi
import com.tkdev.dogs.data.DogsApiMapper
import com.tkdev.dogs.data.model.ApiResponse
import com.tkdev.dogs.data.model.BreedDomain
import com.tkdev.dogs.data.model.DogDomain
import com.tkdev.dogs.data.model.DogModel
import com.tkdev.dogs.repository.DOGS_LIST
import com.tkdev.dogs.repository.DogsRepository
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
    private lateinit var api: DogsApi

    @MockK
    private lateinit var apiMapper: DogsApiMapper

    @MockK
    private lateinit var connectionManager: ConnectionManager

    @MockK
    private lateinit var sharedPreferences: SharedPreferences

    @MockK
    private lateinit var stringWrapper: StringWrapper

    @InjectMockKs
    private lateinit var dogsRepository: DogsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN string resource, WHEN getStringMessage, THEN return message`() {
        //GIVEN
        val expected = "dog has not internet"
        val resId = R.string.exception_no_internet
        every { stringWrapper.getString(resId) } returns expected

        //WHEN
        val result = dogsRepository.getStringMessage(resId)

        //THEN
        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN stored string is not null, WHEN getStoredDogsList, THEN return response success`() {
        //GIVEN
        val storedString = "[{\"id\":${testID},\"dogBreed\":\"${testBreed}\",\"dogSubBreed\":\"${testSubBreed}\",\"dogDisplayName\":\"${testDisplayName}\"}]"

        val expected = ApiResponse.Success(testListDogModel)

        every { sharedPreferences.getString(DOGS_LIST, null) } returns storedString

        //WHEN
        val result = dogsRepository.getStoredDogsList()

        //THEN
        assertEquals(expected.data, result.data)
    }

    @Test
    fun `GIVEN stored string is null, WHEN getStoredDogsList, THEN return response fail`() {
        //GIVEN
        val storedString = null
        val message = "ups, empty"
        val resId = R.string.dogs_list_stored_empty
        val expected = ApiResponse.Fail<List<DogModel>>(message)

        every { stringWrapper.getString(resId) } returns message

        every { sharedPreferences.getString(DOGS_LIST, null) } returns storedString

        //WHEN
        val result = dogsRepository.getStoredDogsList()

        //THEN
        assertEquals(expected.message, result.message)
    }

    @Test
    fun `GIVEN list of DogModel, WHEN storeDogsList, THEN store the list`() {
        //GIVEN
        val storeString = "[{\"id\":${testID},\"dogBreed\":\"${testBreed}\",\"dogSubBreed\":\"${testSubBreed}\",\"dogDisplayName\":\"${testDisplayName}\"}]"

        //WHEN
        dogsRepository.storeDogsList(testListDogModel)

        //THEN
        verify {
            sharedPreferences.edit().putString(DOGS_LIST,storeString)
        }
    }

    @Test
    fun `GIVEN no internet connection, WHEN getDogsList, THEN return response fail`() {
        //GIVEN
        val isConnected = false
        val message = "you shall not pass!"
        val expected = ApiResponse.Fail<List<DogModel>>(message)

        every { connectionManager.checkConnection() } returns isConnected
        every { stringWrapper.getString(R.string.exception_no_internet) } returns message

        //WHEN
        val result = runBlocking { dogsRepository.getDogsList() }

        //THEN
        assertEquals(expected.message, result.message)

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
        coEvery { api.fetchDogsList() } throws exception

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
        coEvery { api.fetchDogsList() } returns dogDomain
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
        every { stringWrapper.getString(R.string.exception_no_internet) } returns message

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
        coEvery { api.fetchDogsList() } throws exception

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
        coEvery { api.fetchBreedDogPictures(testDogModel.dogBreed) } returns breedDomain
        every { apiMapper.mapPicturesWithBreed(breedDomain,testDogModel) } returns expected

        //WHEN
        val result = runBlocking { dogsRepository.getBreedDogPictures(testDogModel) }

        //THEN
        assertEquals(expected.data, result.data)

    }



}