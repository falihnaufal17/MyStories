package com.dicoding.mystories.ui.activity.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystories.repository.UsersRepository
import com.dicoding.mystories.utils.ParseErrorMessage
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _message = MutableLiveData<String?>()
    val message: MutableLiveData<String?> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = usersRepository.postRegister(name, email, password)
                _message.value = response?.message
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = ParseErrorMessage(errorBody).parseErrorMessage()

                _message.value = errorMessage
            } catch (e: Exception) {
                _message.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}