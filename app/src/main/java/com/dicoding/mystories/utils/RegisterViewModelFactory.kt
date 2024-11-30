package com.dicoding.mystories.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystories.repository.UsersRepository
import com.dicoding.mystories.ui.activity.signup.SignUpViewModel

class RegisterViewModelFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(usersRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}