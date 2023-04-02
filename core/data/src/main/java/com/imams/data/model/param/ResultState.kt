package com.imams.data.model.param

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    object Empty: ResultState<Nothing>()
    data class Error(val code: Int, val message: String?) : ResultState<Nothing>()
    class Exception<T>(val e: Throwable) : ResultState<T>()
    data class Loading<T>(val data: T? = null): ResultState<T>()
}