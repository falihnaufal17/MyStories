package com.dicoding.mystories.ui.activity.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mystories.data.response.ListStoryItem
import com.dicoding.mystories.di.StoryInjection
import com.dicoding.mystories.repository.StoriesRepository

class HomeActivityViewModel(storiesRepository: StoriesRepository) : ViewModel() {
    val listStory: LiveData<PagingData<ListStoryItem>> =
        storiesRepository.getStories().cachedIn(viewModelScope)
}

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeActivityViewModel(StoryInjection.provideRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}