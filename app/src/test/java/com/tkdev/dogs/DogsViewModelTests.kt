package com.tkdev.dogs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tkdev.dogs.common.CommonCoroutineDispatcher
import com.tkdev.dogs.common.SingleEvent
import com.tkdev.dogs.data.model.ApiResponse
import com.tkdev.dogs.data.model.DogModel
import com.tkdev.dogs.repository.DogsRepository
import com.tkdev.dogs.utils.*
import com.tkdev.dogs.viewmodels.DogsViewModel
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DogsViewModelTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var dogsRepository: DogsRepository

    @MockK
    private lateinit var coroutineDispatcher: CommonCoroutineDispatcher

    @InjectMockKs
    private lateinit var dogsViewModel: DogsViewModel

    @Before
    fun setup() {
        dogsRepository = mockk()
        coroutineDispatcher = mockk()
        every { coroutineDispatcher.IO } returns Dispatchers.Unconfined
        every { coroutineDispatcher.UI } returns Dispatchers.Unconfined
        dogsViewModel = DogsViewModel(dogsRepository, coroutineDispatcher)
    }

    @Test
    fun `GIVEN string, WHEN setToolbarTitle, THEN update toolbar title`() {
        //GIVEN
        val expected = "toolbar to the top"

        //WHEN
        dogsViewModel.setToolbarTitle(expected)

        //THEN
        val result = dogsViewModel.toolbarTitle.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN success apiResponse, WHEN getDogsList, THEN return dogModel list`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val isRefreshing = false
            val isVisible = false
            val messageSuccessful = "operation success"
            val messageEvent = SingleEvent(messageSuccessful)
            val resId = R.string.dogs_list_successful
            val apiResponse = ApiResponse.Success(testListDogModel)
            val expected = testListDogModel


            every { dogsRepository.getStringMessage(resId) } returns messageSuccessful
            coEvery { dogsRepository.getDogsList() } returns apiResponse
            justRun { apiResponse.data?.let { dogsRepository.storeDogsList(it) } }


            //WHEN
            dogsViewModel.getDogsList()

            //THEN
            val result = dogsViewModel.dogsList.getOrAwaitValue()
            assertEquals(expected, result)
            val messageResult = dogsViewModel.snackBarMessage.getOrAwaitValue()
            assertEquals(messageEvent.peekContent(), messageResult.peekContent())
            val refreshResult = dogsViewModel.isDogListRefreshing.getOrAwaitValue()
            assertEquals(isRefreshing, refreshResult)
            val visibilityResult = dogsViewModel.emptyListVisibility.getOrAwaitValue()
            assertEquals(isVisible, visibilityResult)

            verify {
                dogsRepository.storeDogsList(testListDogModel)
            }
        }

    @Test
    fun `GIVEN fail apiResponse AND success localResponse, WHEN getDogsList, THEN return dogModel list`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val isRefreshing = false
            val isVisible = false
            val messageApiFail = "nope, missing internets"
            val messageSuccessful = "operation success locally"
            val messageEvent = SingleEvent(messageSuccessful)
            val resId = R.string.dogs_list_successful_local
            val apiResponse = ApiResponse.Fail(messageApiFail, emptyList<DogModel>())
            val localResponse = ApiResponse.Success(testListDogModel)
            val expected = testListDogModel


            every { dogsRepository.getStringMessage(resId) } returns messageSuccessful
            coEvery { dogsRepository.getDogsList() } returns apiResponse
            every { dogsRepository.getStoredDogsList() } returns localResponse
            justRun { localResponse.data?.let { dogsRepository.storeDogsList(it) } }


            //WHEN
            dogsViewModel.getDogsList()

            //THEN
            val result = dogsViewModel.dogsList.getOrAwaitValue()
            assertEquals(expected, result)
            val messageResult = dogsViewModel.snackBarMessage.getOrAwaitValue()
            assertEquals(messageEvent.peekContent(), messageResult.peekContent())
            val refreshResult = dogsViewModel.isDogListRefreshing.getOrAwaitValue()
            assertEquals(isRefreshing, refreshResult)
            val visibilityResult = dogsViewModel.emptyListVisibility.getOrAwaitValue()
            assertEquals(isVisible, visibilityResult)

            verify {
                localResponse.data?.let { dogsRepository.storeDogsList(it) }
            }
        }

    @Test
    fun `GIVEN fail apiResponse AND fail localResponse, WHEN getDogsList, THEN return empty list`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val isRefreshing = false
            val isVisible = true
            val messageApiFail = "nope, missing internets"
            val messageLocalFail = "operation fail locally"
            val messageEvent = SingleEvent(messageApiFail)
            val apiResponse = ApiResponse.Fail(messageApiFail, emptyList<DogModel>())
            val localResponse =  ApiResponse.Fail(messageLocalFail, emptyList<DogModel>())
            val expected = emptyList<DogModel>()

            coEvery { dogsRepository.getDogsList() } returns apiResponse
            every { dogsRepository.getStoredDogsList() } returns localResponse
            justRun { localResponse.data?.let { dogsRepository.storeDogsList(it) } }


            //WHEN
            dogsViewModel.getDogsList()

            //THEN
            val result = dogsViewModel.dogsList.getOrAwaitValue()
            assertEquals(expected, result)
            val messageResult = dogsViewModel.snackBarMessage.getOrAwaitValue()
            assertEquals(messageEvent.peekContent(), messageResult.peekContent())
            val refreshResult = dogsViewModel.isDogListRefreshing.getOrAwaitValue()
            assertEquals(isRefreshing, refreshResult)
            val visibilityResult = dogsViewModel.emptyListVisibility.getOrAwaitValue()
            assertEquals(isVisible, visibilityResult)

            verify {
                localResponse.data?.let { dogsRepository.storeDogsList(it) }
            }
        }

    @Test
    fun `GIVEN no dogModel, WHEN getBreedDogPictures, THEN return response fail`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val isRefreshing = false
            val dogModel : DogModel? = null
            val messageFail = "no doggo"
            val resId = R.string.exception_fetch_fail
            val expected = SingleEvent(messageFail)

            every { dogsRepository.getStringMessage(resId) } returns messageFail

            //WHEN
            dogsViewModel.getBreedDogPictures(dogModel)

            //THEN
            val messageResult = dogsViewModel.snackBarMessage.getOrAwaitValue()
            assertEquals(expected.peekContent(), messageResult.peekContent())
            val refreshResult = dogsViewModel.isDogListRefreshing.getOrAwaitValue()
            assertEquals(isRefreshing, refreshResult)

        }

    @Test
    fun `GIVEN dogModel AND success apiResponse, WHEN getBreedDogPictures, THEN return response success`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val isRefreshing = false
            val messageSuccessful = "operation success"
            val messageEvent = SingleEvent(messageSuccessful)
            val resId = R.string.pictures_list_successful
            val expected = testlistPictureUrl
            val apiResponse = ApiResponse.Success(expected)

            every { dogsRepository.getStringMessage(resId) } returns messageSuccessful
            coEvery { dogsRepository.getBreedDogPictures(testDogModel) } returns apiResponse


            //WHEN
            dogsViewModel.getBreedDogPictures(testDogModel)

            //THEN
            val result = dogsViewModel.breedDogPictures.getOrAwaitValue()
            assertEquals(expected, result.peekContent())
            val messageResult = dogsViewModel.snackBarMessage.getOrAwaitValue()
            assertEquals(messageEvent.peekContent(), messageResult.peekContent())
            val refreshResult = dogsViewModel.isDogListRefreshing.getOrAwaitValue()
            assertEquals(isRefreshing, refreshResult)

        }

    @Test
    fun `GIVEN dogModel AND fail apiResponse, WHEN getBreedDogPictures, THEN return response fail`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            val isRefreshing = false
            val messageFail = "no pictures today"
            val messageEvent = SingleEvent(messageFail)
            val resId = R.string.exception_fetch_fail
            val expected = emptyList<String>()
            val apiResponse = ApiResponse.Fail(messageFail, expected)

            every { dogsRepository.getStringMessage(resId) } returns messageFail
            coEvery { dogsRepository.getBreedDogPictures(testDogModel) } returns apiResponse


            //WHEN
            dogsViewModel.getBreedDogPictures(testDogModel)

            //THEN
            val result = dogsViewModel.breedDogPictures.getOrAwaitValue()
            assertEquals(expected, result.peekContent())
            val messageResult = dogsViewModel.snackBarMessage.getOrAwaitValue()
            assertEquals(messageEvent.peekContent(), messageResult.peekContent())
            val refreshResult = dogsViewModel.isDogListRefreshing.getOrAwaitValue()
            assertEquals(isRefreshing, refreshResult)

        }
}
