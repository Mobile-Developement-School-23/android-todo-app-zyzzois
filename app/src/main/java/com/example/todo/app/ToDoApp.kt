package com.example.todo.app

import android.app.Application
import com.example.todo.di.DaggerApplicationComponent
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken

class ToDoApp: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    private val sdk by lazy {
        YandexAuthSdk(this, YandexAuthOptions( this,  true))
    }

    private val loggingOptionsBuilder by lazy {
        YandexAuthLoginOptions.Builder()
    }

    private val authIntent by lazy {
        sdk.createLoginIntent(loggingOptionsBuilder.build())
    }

   // val res = sdk.getJwt("y0_AgAAAABEww8dAAoW1AAAAADmPgw8qnY-Ic0-RbqS2v0vK-8qtP2eaFM" as YandexAuthToken)

}