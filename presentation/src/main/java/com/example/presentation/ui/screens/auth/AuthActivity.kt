package com.example.presentation.ui.screens.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.presentation.R
import com.example.presentation.databinding.ActivityAuthBinding
import com.example.presentation.di.PresentationComponentProvider
import com.example.presentation.ui.core.factories.ViewModelFactory
import com.example.presentation.ui.screens.main.MainActivity
import com.example.presentation.ui.util.Constants.AUTH_FAILED
import com.example.presentation.ui.util.Constants.AUTH_STATE
import com.example.presentation.ui.util.Constants.AUTH_SUCCESS
import com.example.presentation.ui.util.Constants.AUTH_TABLE_NAME
import com.example.presentation.ui.util.Constants.AUTH_TOKEN
import com.example.presentation.ui.util.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as PresentationComponentProvider).providePresentationComponent().create()
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
    }

    private val authPreferences by lazy {
        getSharedPreferences(AUTH_TABLE_NAME, Context.MODE_PRIVATE)
    }
    private val authPreferencesEditor by lazy { authPreferences.edit() }

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
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeViewModel()
        //checkAuthState()
        setupAuthResultObserver()
        setupButtons()
    }

    private fun observeViewModel() {
        viewModel.alreadyAuthorized.observe(this) { authorized ->
            if (authorized) {
                binding.imageView.visibility = View.VISIBLE
                binding.buttonNext.text = "Выйти"
            } else {
                binding.imageView.visibility = View.GONE
            }
        }
    }

    private fun checkAuthState() {
        if(authPreferences.getBoolean(AUTH_STATE, false)) {
            showToast(AUTH_SUCCESS)
            startActivity(MainActivity.newIntentOpenMainActivity(this))
        } else showToast(AUTH_FAILED)
    }

    private fun setupAuthResultObserver() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                val yandexAuthToken: YandexAuthToken? = sdk.extractToken(it.resultCode, it.data)
                if (yandexAuthToken != null) {
                    viewModel.changeAuthStatus(true)
                    authPreferencesEditor.putString(AUTH_TOKEN, yandexAuthToken.value)
                    authPreferencesEditor.apply()
                    showToast(AUTH_SUCCESS)
                    authPreferencesEditor.putBoolean(AUTH_STATE, true)
                    authPreferencesEditor.apply()
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
            MaterialAlertDialogBuilder(this@AuthActivity)
                .setTitle(resources.getString(R.string.warning))
                .setMessage(resources.getString(R.string.warning_content))
                .setPositiveButton(resources.getString(R.string.authorize)) { _, _ ->
                    launcher?.launch(authIntent)
                }
                .setNeutralButton(resources.getString(R.string.ignore)) { _, _ ->
                    showSecondDialog()
                }
                .show()

        }
    }

    private fun showSecondDialog() {
        MaterialAlertDialogBuilder(this@AuthActivity)
            .setTitle(resources.getString(R.string.notice))
            .setMessage(resources.getString(R.string.warning2))
            .setPositiveButton(resources.getString(R.string.OkText)) { _, _ ->
                startActivity(MainActivity.newIntentOpenMainActivity(this@AuthActivity))
            }
            .show()
    }


    companion object {
        fun newIntentOpenAuthActivity(context: Context) = Intent(context, AuthActivity::class.java)
    }

}