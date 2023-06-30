package com.example.todo.ui.screens.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.todo.R
import com.example.todo.databinding.ActivityAuthBinding
import com.example.todo.ui.screens.main.MainActivity
import com.example.todo.ui.util.Constants
import com.example.todo.ui.util.Constants.AUTH_FAILED
import com.example.todo.ui.util.Constants.AUTH_STATE
import com.example.todo.ui.util.Constants.AUTH_SUCCESS
import com.example.todo.ui.util.Constants.AUTH_TABLE_NAME
import com.example.todo.ui.util.Constants.AUTH_TOKEN_TABLE_NAME
import com.example.todo.ui.util.showToast
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread


class AuthActivity : AppCompatActivity() {

    private val isAlreadyAuthorized by lazy {
        getSharedPreferences(AUTH_TABLE_NAME, Context.MODE_PRIVATE)
    }
    private val authStateEditor by lazy { isAlreadyAuthorized.edit() }

    private val personalAuthToken by lazy {
        getSharedPreferences(this.getString(R.string.auth_token_table_name), Context.MODE_PRIVATE)
    }
    private val personalAuthTokenEditor by lazy { personalAuthToken.edit() }

    private val binding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
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

    private var launcher: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //checkAuthState()
        setupAuthResultObserver()
        setupButtons()
    }

    private fun checkAuthState() {
        if(isAlreadyAuthorized.getBoolean(AUTH_STATE, false)) {
            showToast(AUTH_SUCCESS)
            startActivity(MainActivity.newIntentOpenMainActivity(this))
        } else showToast(AUTH_FAILED)
    }

    private fun setupAuthResultObserver() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                val yandexAuthToken: YandexAuthToken? = sdk.extractToken(it.resultCode, it.data)
                if (yandexAuthToken != null) {
                    personalAuthTokenEditor.putString(KEY, yandexAuthToken.value)
                    personalAuthTokenEditor.apply()
                    showToast(AUTH_SUCCESS)
                    authStateEditor.putBoolean(AUTH_STATE, true)
                    authStateEditor.apply()
                    startActivity(MainActivity.newIntentOpenMainActivity(this))
                }
            } catch (e: YandexAuthException) {
                showToast(AUTH_FAILED)
            }
        }
    }

    private fun setupButtons() = with(binding) {
        buttonAuthViaYandex.setOnClickListener {
            launcher?.launch(authIntent)
        }

        buttonNext.setOnClickListener {
            startActivity(MainActivity.newIntentOpenMainActivity(this@AuthActivity))
        }
    }

    companion object {
        const val KEY = "auth_token_key"
    }

}