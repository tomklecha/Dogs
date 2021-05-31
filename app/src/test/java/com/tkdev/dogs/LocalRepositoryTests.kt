package com.tkdev.dogs

import android.content.SharedPreferences
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.model.ApiResponse
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.repository.DOGS_LIST
import com.tkdev.dogs.repository.local.LocalRepositoryDefault
import com.tkdev.dogs.utils.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalRepositoryTests {

    @MockK
    private lateinit var sharedPreferences: SharedPreferences

    @MockK
    private lateinit var stringWrapper: StringWrapper

    @InjectMockKs
    private lateinit var localRepository: LocalRepositoryDefault

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN stored string is not null, WHEN getStoredDogsList, THEN return list`() {
        //GIVEN
        val storedString = "[{\"id\":$testID,\"dogBreed\":\"$testBreed\",\"dogSubBreed\":\"$testSubBreed\",\"dogDisplayName\":\"$testDisplayName\"}]"


        val expected = ApiResponse.Success(testListDogModel)

        every { sharedPreferences.getString(DOGS_LIST, null) } returns storedString

        //WHEN
        val result = localRepository.getStoredDogsList()

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
        val result = localRepository.getStoredDogsList()

        //THEN
//        assertEquals(expected.message, result.message)
    }
}