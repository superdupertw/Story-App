package com.dicoding.storyapplication.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapplication.data.remote.response.Story
import com.dicoding.storyapplication.data.remote.retrofit.ApiService
import com.dicoding.storyapplication.view.login.LoginPreferences

class StoryPagingSource(
    private val preferences: LoginPreferences,
    private val apiServices: ApiService
) : PagingSource<Int, Story>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val tokens = preferences.getUser().token.toString()
            val pages = params.key ?: INITIAL_PAGE_INDEX
            if (tokens.isNotEmpty()) {
                val dataResponse =
                    tokens.let { apiServices.getStory("Bearer $it", pages, params.loadSize, 0) }
                if (dataResponse.isSuccessful) {
                    LoadResult.Page(
                        data = dataResponse.body()?.listStory ?: emptyList(),
                        prevKey = if (pages == INITIAL_PAGE_INDEX) null else pages - 1,
                        nextKey = if (dataResponse.body()?.listStory.isNullOrEmpty()) null else pages + 1
                    )
                } else {
                    LoadResult.Error(Exception("Failed load story"))
                }
            } else {
                LoadResult.Error(Exception("Token empty"))
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val pageAnchor = state.closestPageToPosition(it)
            pageAnchor?.prevKey?.plus(1) ?: pageAnchor?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}