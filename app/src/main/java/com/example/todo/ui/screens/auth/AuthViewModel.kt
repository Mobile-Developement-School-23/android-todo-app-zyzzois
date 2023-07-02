package com.example.todo.ui.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class AuthViewModel @Inject constructor(): ViewModel() {

    private val _alreadyAuthorized = MutableLiveData<Boolean>()
    val alreadyAuthorized: LiveData<Boolean>
        get() = _alreadyAuthorized


    fun changeAuthStatus(authorized: Boolean) {
        _alreadyAuthorized.value = authorized
    }
}