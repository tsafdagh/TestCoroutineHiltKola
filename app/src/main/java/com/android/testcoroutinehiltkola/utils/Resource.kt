package com.android.testcoroutinehiltkola.utils


sealed class Resource<out T> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Loading<out T>(val partialData: T? = null) : Resource<T>()
    data class Failure<out T>(val throwable: Throwable? = null, val partialDataFaillure: T?) : Resource<T>()

    val extractData: T? get() = when (this) {
        is Success -> data
        is Loading -> partialData
        is Failure -> partialDataFaillure
    }


}

sealed class FirebaseResponseType<out T> {

    data class FirebaseSuccesResponse<T>(val body: T) : FirebaseResponseType<T>(){}

    data class FirebaseErrorResponse<T>(val throwable: Throwable? = null) : FirebaseResponseType<T>()

    data class FirebaseEmptyResponse<T>(val body: T?) : FirebaseResponseType<T>()
}