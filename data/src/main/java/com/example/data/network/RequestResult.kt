package com.example.data.network

import retrofit2.Response

sealed class RequestResult<out T> {
    data class SUCCESS<T>(val value: T): RequestResult<T>()
    data class ERROR<T>(val e: RequestError): RequestResult<T>()
}

enum class RequestError(val code: Int) {
    SERVER(500),
    REVISION(400),
    NOT_FOUND(404),
    UNDEFINED_ERROR(0),
    AUTH(401)
}

inline fun <reified T> Response<T>.result(): RequestResult<T> {
    if (isSuccessful) {
        val responseBody: T? = body()
        return if (responseBody != null) {
            RequestResult.SUCCESS(responseBody)
        } else {
            RequestResult.ERROR(RequestError.UNDEFINED_ERROR)
        }
    } else {
        val error = when (code()) {
            400 -> RequestError.REVISION
            401 -> RequestError.AUTH
            404 -> RequestError.NOT_FOUND
            500 -> RequestError.SERVER
            else -> RequestError.UNDEFINED_ERROR
        }
        return RequestResult.ERROR(error)
    }
}


