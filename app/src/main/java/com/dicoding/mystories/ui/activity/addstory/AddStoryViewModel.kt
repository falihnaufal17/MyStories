package com.dicoding.mystories.ui.activity.addstory

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.mystories.di.StoryInjection
import com.dicoding.mystories.repository.StoriesRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    private val _preview = MutableLiveData<Uri?>()
    val preview: LiveData<Uri?> = _preview

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun postStory(description: RequestBody, photo: MultipartBody.Part) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = storiesRepository.postStory(description, photo)
                _message.value = response?.message
                _isError.value = false
            } catch (e: HttpException) {
                _isError.value = true
                _message.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun savePreview(photo: Uri?) {
        _preview.value = photo
    }
}

class AddStoryViewModelFactory (
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(StoryInjection.provideRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}