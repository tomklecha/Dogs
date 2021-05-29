package com.tkdev.dogs

import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.repository.remote.DogsRemoteRepoMapperDefault
import com.tkdev.dogs.model.ApiResponse
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.utils.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DogsApiMapperTests {

    @MockK
    private lateinit var stringWrapper: StringWrapper

    @InjectMockKs
    private lateinit var apiMapper: DogsRemoteRepoMapperDefault

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN empty dogs map, WHEN mapDogsList, THEN return response fail`() {
        //GIVEN
        val dogMap = mapOf<String, List<String>>()
        val message = "emptiness"
        val emptyList = emptyList<DogModel>()
        val expected = ApiResponse.Fail(message, emptyList)

        every { stringWrapper.getString(R.string.mapper_list_empty) } returns message

        //WHEN
        val result = apiMapper.mapDogsList(dogMap)

        //THEN
        assertEquals(expected.message, result.message)
        assertEquals(expected.data, emptyList)
    }

    @Test
    fun `GIVEN dogs map without subbreed, WHEN mapDogsList, THEN return response success list with subbreed `() {
        //GIVEN
        val id = 0
        val breed = testBreed
        val subBreed = ""
        val displayName = breed.capitalize()
        val list = listOf(DogModel(id, breed, subBreed, displayName))
        val expected = ApiResponse.Success(list)

        //WHEN
        val result = apiMapper.mapDogsList(testDogMap)

        //THEN
        assertEquals(expected.data, result.data)
    }

    @Test
    fun `GIVEN dogs map with subbreed, WHEN mapDogsList, THEN return response success list with subbreed `() {
        //GIVEN
        val id = 0
        val breed = testBreed
        val subBreed = testSubBreed
        val displayName = "${subBreed.capitalize()} ${breed.capitalize()}"
        val list = listOf(DogModel(id, breed, subBreed, displayName))
        val expected = ApiResponse.Success(list)

        //WHEN
        val result = apiMapper.mapDogsList(testDogMapWithSubBreed)

        //THEN
        assertEquals(expected.data, result.data)
    }

    @Test
    fun `GIVEN empty breed domain, WHEN mapPicturesWithBreed, THEN return response fail`() {
        //GIVEN
        val message = "emptiness"
        val emptyList = emptyList<String>()
        val expected = ApiResponse.Fail(message, emptyList)

        every { stringWrapper.getString(R.string.mapper_list_empty) } returns message

        //WHEN
        val result = apiMapper.mapPicturesWithBreed(testBreedDomainEmpty, testDogModel)

        //THEN
        assertEquals(expected.message, result.message)
        assertEquals(expected.data, emptyList)
    }

    @Test
    fun `GIVEN breed domain list below ten, WHEN mapPicturesWithBreed, THEN return response success`() {
        //GIVEN
        val expected = ApiResponse.Success(testlistPictureUrl)

        //WHEN
        val result = apiMapper.mapPicturesWithBreed(testBreedDomain, testDogModel)

        //THEN
        assertEquals(expected.data, result.data)
    }

    @Test
    fun `GIVEN breed domain list above ten, WHEN mapPicturesWithBreed, THEN return response success with 10 elements`() {
        //GIVEN
        val expected = ApiResponse.Success(testlistTenPictureUrl)

        //WHEN
        apiMapper.mapPicturesWithBreed(testBreedDomainAboveTenElements, testDogModel)

        //THEN
        assertEquals(expected.data?.size, 10)
    }
}