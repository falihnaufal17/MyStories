package com.dicoding.mystories.di

import android.content.Context
import com.dicoding.mystories.data.database.StoryDatabase
import com.dicoding.mystories.data.local.UserPreference
import com.dicoding.mystories.data.retrofit.ApiConfig
import com.dicoding.mystories.repository.StoriesRepository

object StoryInjection {
    fun provideRepository(context: Context): StoriesRepository {
        val userPreference = UserPreference(context)
        val token = userPreference.getUser().token ?: ""
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(token)

        return StoriesRepository(database, apiService)
    }
}