package com.dicoding.storyapplication.view.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.storyapplication.data.StoryRepository

class LoginViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) : ViewModel() {
    private val repositories = StoryRepository(application, loginPreferences)

    fun userLogin(email: String, password: String) = repositories.login(email, password)
}