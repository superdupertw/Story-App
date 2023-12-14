package com.dicoding.storyapplication.view.liststory

import androidx.lifecycle.ViewModel
import com.dicoding.storyapplication.data.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStory() = storyRepository.listStory()
}