package com.dicoding.mystories.ui.activity.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystories.data.response.LoginResult
import com.dicoding.mystories.repository.UsersRepository
import com.dicoding.mystories.utils.ParseErrorMessage
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.math.log

class SignInViewModel (
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _message = MutableLiveData<String?>()
    val message: MutableLiveData<String?> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> = _loginResult

    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = usersRepository.postLogin(email, password)
                _message.value = response?.message
                _loginResult.value = response?.loginResult
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = ParseErrorMessage(errorBody).parseErrorMessage()

                _message.value = errorMessage
            } catch (e: Exception) {
                _message.value = "An unexpected error occurred: ${e.message}"
            }  finally {
                _isLoading.value = false
            }
        }
    }
}