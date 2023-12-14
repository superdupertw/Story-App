package com.dicoding.storyapplication.view.map

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.storyapplication.data.StoryRepository
import com.dicoding.storyapplication.view.login.LoginPreferences

class MapViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) : ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    fun getStories() = repository.listStoryLoc()
}