package com.dicoding.mystories.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.mystories.data.StoryRemoteMediator
import com.dicoding.mystories.data.database.StoryDatabase
import com.dicoding.mystories.data.response.AddStoryResponse
import com.dicoding.mystories.data.response.DetailStoryResponse
import com.dicoding.mystories.data.response.ListStoryItem
import com.dicoding.mystories.data.response.ListStoryResponse
import com.dicoding.mystories.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.awaitResponse

class StoriesRepository (
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    suspend fun postStory(description: RequestBody, photo: MultipartBody.Part): AddStoryResponse? {
        val response = apiService.addStory(photo, description).awaitResponse()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw HttpException(response)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun getDetailStory(id: String): DetailStoryResponse? {
        val response = apiService.getDetailStory(id).awaitResponse()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getStoriesMap(): List<ListStoryItem> {
        val response = apiService.getStories(1, 10, 1)

        return response.listStory
    }
}