package com.example.todo.app

import android.app.Application
import com.example.todo.di.DaggerApplicationComponent

class ToDoApp: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}