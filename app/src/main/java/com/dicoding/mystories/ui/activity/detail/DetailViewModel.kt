package com.dicoding.mystories.ui.activity.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.mystories.data.response.Story
import com.dicoding.mystories.di.StoryInjection
import com.dicoding.mystories.repository.StoriesRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel (private val storiesRepository: StoriesRepository) : ViewModel() {
    private val _detail = MutableLiveData<Story?>()
    val detail: LiveData<Story?> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailStory(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = storiesRepository.getDetailStory(id)
                _detail.value = response?.story
            } catch (e: HttpException) {
                _detail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class DetailViewModelFactory (
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(StoryInjection.provideRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}