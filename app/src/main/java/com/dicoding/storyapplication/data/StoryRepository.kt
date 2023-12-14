package com.dicoding.storyapplication.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapplication.data.remote.response.InsertStoryResponse
import com.dicoding.storyapplication.data.remote.response.LoginResponse
import com.dicoding.storyapplication.data.remote.response.RegisterResponse
import com.dicoding.storyapplication.data.remote.response.Story
import com.dicoding.storyapplication.data.remote.response.StoryResponse
import com.dicoding.storyapplication.data.remote.retrofit.ApiConfig
import com.dicoding.storyapplication.data.remote.retrofit.ApiService
import com.dicoding.storyapplication.view.login.LoginPreferences
import com.dicoding.storyapplication.view.setting.SettingPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    application: Application,
    private val loginPreferences: LoginPreferences
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val apiServices: ApiService = ApiConfig.getApiService()
    private val preferences: SettingPreferences

    init {
        preferences = SettingPreferences.getInstance(application.dataStore)
    }

    suspend fun saveTheme(darkMode: Boolean) = preferences.saveThemeSetting(darkMode)

    fun getTheme() = preferences.getThemeSetting()

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val responses = apiServices.register(
                name,
                email,
                password
            )
            if (responses.error) {
                emit(Result.Error(responses.message))
            } else {
                emit(Result.Success(responses))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val responses = apiServices.login(
                email,
                password
            )
            if (responses.error) {
                emit(Result.Error(responses.message))
            } else {
                emit(Result.Success(responses))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun listStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(loginPreferences, apiServices)
            }
        ).liveData
    }

    fun listStoryLoc(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val responses = apiServices.getStoryLoc(
                token = "Bearer ${loginPreferences.getUser().token}",
                page = 1,
                size = 100,
                location = 1
            )
            if (responses.error) {
                emit(Result.Error(responses.message))
            } else {
                emit(Result.Success(responses))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun insertStory(
        imageFile: MultipartBody.Part,
        desc: RequestBody,
        lat: Double,
        lon: Double
    ): LiveData<Result<InsertStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val responses = apiServices.insertStory(
                token = "Bearer ${loginPreferences.getUser().token}",
                file = imageFile,
                description = desc,
                lat = lat,
                lon = lon
            )
            if (responses.error) {
                emit(Result.Error(responses.message))
            } else {
                emit(Result.Success(responses))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}