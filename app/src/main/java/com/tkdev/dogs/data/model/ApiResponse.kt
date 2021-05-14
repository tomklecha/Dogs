package com.tkdev.dogs.data.model

sealed class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ApiResponse<T>(data)
    class Fail<T>(message: String, data: T? = null) : ApiResponse<T>(data, message)
}