package com.example.data.network

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class Interceptor @Inject constructor(private val prefs: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "OAuth " + prefs.getString(AUTH_TOKEN, DEFAULT_VALUE)
        val request = chain
            .request()
            .newBuilder()
            .addHeader(HEADER_NAME, token)
            .build()
        return chain.proceed(request)
    }

    companion object {
        const val HEADER_NAME = "Authorization"
        const val AUTH_TOKEN = "auth token"
        const val DEFAULT_VALUE = "OAuth y0_AgAAAABEww8dAAoW1AAAAADmPgw8qnY-Ic0-RbqS2v0vK-8qtP2eaFM"
    }
}
