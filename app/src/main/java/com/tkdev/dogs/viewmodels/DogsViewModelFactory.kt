package com.tkdev.dogs.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tkdev.dogs.common.CommonCoroutineDispatcher
import com.tkdev.dogs.repository.DogsRepository

class DogsViewModelFactory(
    private val repository: DogsRepository,
    private val coroutineDispatcher: CommonCoroutineDispatcher
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DogsViewModel(repository, coroutineDispatcher) as T
    }
}