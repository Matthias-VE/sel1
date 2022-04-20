package com.heppihome.data.models

sealed class ResultState<out T> {
    class Loading<out T> : ResultState<T>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Failed<out T>(val message : String) : ResultState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data : T) = Success<T>(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}