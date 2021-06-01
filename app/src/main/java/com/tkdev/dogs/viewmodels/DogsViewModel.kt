package com.tkdev.dogs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkdev.dogs.R
import com.tkdev.dogs.common.CommonCoroutineDispatcher
import com.tkdev.dogs.common.SingleEvent
import com.tkdev.dogs.common.StringWrapper
import com.tkdev.dogs.model.ApiResponse
import com.tkdev.dogs.model.DogModel
import com.tkdev.dogs.repository.DogsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogsViewModel @Inject constructor(
    private val repository: DogsRepository,
    private val coroutineDispatcher: CommonCoroutineDispatcher,
    private val stringWrapper: StringWrapper
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
            when (val response = repository.getDogsList()) {
                is ApiResponse.Success -> {
                    _emptyListVisibility.postValue(false)
                    _dogsList.postValue(response.data)
                    _isDogListRefreshing.postValue(false)
                    _snackBarMessage.postValue(SingleEvent(stringWrapper.getString(R.string.dogs_list_successful)))
                }
                is ApiResponse.Fail -> {
                    _dogsList.postValue(response.data)
                    if (response.data?.isNotEmpty() == true)
                        _emptyListVisibility.postValue(false)
                    _isDogListRefreshing.postValue(false)
                    _snackBarMessage.postValue(
                        SingleEvent(
                            response.message
                                ?: stringWrapper.getString(R.string.exception_fetch_fail)
                        )
                    )
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
                        _snackBarMessage.postValue(SingleEvent(stringWrapper.getString(R.string.pictures_list_successful)))
                        _isDogListRefreshing.postValue(false)
                    }
                    is ApiResponse.Fail -> {
                        _snackBarMessage.postValue(
                            SingleEvent(
                                result.message
                                    ?: stringWrapper.getString(R.string.exception_fetch_fail)
                            )
                        )
                        _isDogListRefreshing.postValue(false)
                    }
                }
                _breedDogPictures.postValue(SingleEvent(result.data))
            } else {
                _snackBarMessage.postValue(
                    SingleEvent(
                        stringWrapper.getString(R.string.exception_fetch_fail)
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