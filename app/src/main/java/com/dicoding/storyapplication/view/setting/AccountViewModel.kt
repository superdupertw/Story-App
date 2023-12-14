package com.dicoding.storyapplication.view.setting

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapplication.data.StoryRepository
import com.dicoding.storyapplication.view.login.LoginPreferences
import kotlinx.coroutines.launch

class AccountViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) : ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    fun getThemeSettings() = repository.getTheme().asLiveData()

    fun saveThemeSettings(isDarkMode: Boolean) {
        viewModelScope.launch {
            repository.saveTheme(isDarkMode)
        }
    }
}