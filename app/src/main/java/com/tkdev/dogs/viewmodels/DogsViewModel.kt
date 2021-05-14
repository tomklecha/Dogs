package com.tkdev.dogs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkdev.dogs.R
import com.tkdev.dogs.common.CommonCoroutineDispatcher
import com.tkdev.dogs.common.SingleEvent
import com.tkdev.dogs.data.model.ApiResponse
import com.tkdev.dogs.data.model.DogModel
import com.tkdev.dogs.repository.DogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DogsViewModel(
    private val repository: DogsRepository,
    private val coroutineDispatcher: CommonCoroutineDispatcher
) : ViewModel() {

    private val _dogsList = MutableLiveData<List<DogModel>?>()

    private val _breedDogPictures = MutableLiveData<SingleEvent<List<String>?>>()

    private val _emptyListVisibility = MutableLiveData(true)

    private val _isDogListRefreshing = MutableLiveData<Boolean>()

    private val _snackBarMessage = MutableLiveData<SingleEvent<String>>()

    private val _toolbarTitle = MutableLiveData<String?>()

    val dogsList: LiveData<List<DogModel>?> get() = _dogsList

    val breedDogPictures: LiveData<SingleEvent<List<String>?>> get() = _breedDogPictures

    val isDogListRefreshing: LiveData<Boolean> get() = _isDogListRefreshing

    val emptyListVisibility: LiveData<Boolean> get() = _emptyListVisibility

    val snackBarMessage: LiveData<SingleEvent<String>> get() = _snackBarMessage

    val toolbarTitle: LiveData<String?> get() = _toolbarTitle

    fun getDogsList() {
        viewModelScope.launch(coroutineDispatcher.IO) {
            _isDogListRefreshing.postValue(true)
            when (val apiResponse = repository.getDogsList()) {
                is ApiResponse.Success -> {
                    _emptyListVisibility.postValue(false)
                    _dogsList.postValue(apiResponse.data)
                    _snackBarMessage.postValue(SingleEvent(repository.getStringMessage(R.string.dogs_list_successful)))
                    _isDogListRefreshing.postValue(false)
                    apiResponse.data?.let { repository.storeDogsList(it) }
                }
                is ApiResponse.Fail -> {
                    val localResponse = repository.getStoredDogsList()
                    when (localResponse) {
                        is ApiResponse.Success -> {
                            _emptyListVisibility.postValue(false)
                            _snackBarMessage.postValue(
                                SingleEvent(
                                    repository.getStringMessage(
                                        R.string.dogs_list_successful_local
                                    )
                                )
                            )
                            _isDogListRefreshing.postValue(false)
                        }
                        is ApiResponse.Fail -> {
                            _snackBarMessage.postValue(
                                SingleEvent(
                                    apiResponse.message
                                        ?: localResponse.message
                                        ?: repository.getStringMessage(R.string.exception_fetch_fail)
                                )
                            )
                            _isDogListRefreshing.postValue(false)
                        }
                    }
                    _dogsList.postValue(localResponse.data)
                    localResponse.data?.let { repository.storeDogsList(it) }
                }
            }
        }
    }

    fun getBreedDogPictures(dogModel: DogModel?) {
        viewModelScope.launch(coroutineDispatcher.IO) {
            if (dogModel != null) {
                val result = repository.getBreedDogPictures(dogModel)
                when (result) {
                    is ApiResponse.Success -> {
                        _snackBarMessage.postValue(SingleEvent(repository.getStringMessage(R.string.pictures_list_successful)))
                        _isDogListRefreshing.postValue(false)
                    }
                    is ApiResponse.Fail -> {
                        _snackBarMessage.postValue(
                            SingleEvent(
                                result.message
                                    ?: repository.getStringMessage(R.string.exception_fetch_fail)
                            )
                        )
                        _isDogListRefreshing.postValue(false)
                    }
                }
                _breedDogPictures.postValue(SingleEvent(result.data))
            } else {
                _snackBarMessage.postValue(
                    SingleEvent(
                        repository.getStringMessage(R.string.exception_fetch_fail)
                    )
                )
                _isDogListRefreshing.postValue(false)
            }
        }
    }

    fun setToolbarTitle(title: String?) {
        _toolbarTitle.postValue(title)
    }
}