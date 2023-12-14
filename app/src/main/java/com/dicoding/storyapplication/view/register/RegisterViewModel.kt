package com.dicoding.storyapplication.view.register

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.storyapplication.data.StoryRepository
import com.dicoding.storyapplication.view.login.LoginPreferences

class RegisterViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) :
    ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    fun userRegister(name: String, email: String, password: String) =
        repository.register(name, email, password)
}