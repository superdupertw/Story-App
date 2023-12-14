package com.dicoding.storyapplication.view.liststory

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapplication.data.StoryRepository
import com.dicoding.storyapplication.data.remote.response.Story
import com.dicoding.storyapplication.view.login.LoginPreferences

class ListStoryViewModel(
    application: Application,
    loginPreferences: LoginPreferences
) : ViewModel() {
    private val repository = StoryRepository(application, loginPreferences)

    val getListStory: LiveData<PagingData<Story>> = repository.listStory().cachedIn(viewModelScope)
}