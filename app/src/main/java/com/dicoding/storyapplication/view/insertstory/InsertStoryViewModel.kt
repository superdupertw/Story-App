package com.dicoding.storyapplication.view.insertstory

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.storyapplication.data.StoryRepository
import com.dicoding.storyapplication.view.login.LoginPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody

class InsertStoryViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) :
    ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    fun insertStory(imageFile: MultipartBody.Part, desc: RequestBody, lat: Double, lon: Double) =
        repository.insertStory(imageFile, desc, lat, lon)
}