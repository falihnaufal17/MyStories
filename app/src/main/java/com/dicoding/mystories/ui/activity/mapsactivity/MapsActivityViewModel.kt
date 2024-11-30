package com.dicoding.mystories.ui.activity.mapsactivity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.mystories.data.response.ListStoryItem
import com.dicoding.mystories.di.StoryInjection
import com.dicoding.mystories.repository.StoriesRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsActivityViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            getStories()
        }
    }

    private suspend fun getStories() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = storiesRepository.getStoriesMap()
                if (response != null) {
                    _listStory.value = response.listStory
                }
            } catch (e: HttpException) {
                _listStory.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class MapsViewModelFactory (
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsActivityViewModel::class.java)) {
            return MapsActivityViewModel(StoryInjection.provideRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}