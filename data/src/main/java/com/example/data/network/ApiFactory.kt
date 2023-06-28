package com.example.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"

    private var okHttpClient: OkHttpClient

     init {
         val httpLoggingInterceptor = HttpLoggingInterceptor()
         httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
         okHttpClient = OkHttpClient.Builder()
             .addInterceptor(httpLoggingInterceptor)
             .build()
     }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}