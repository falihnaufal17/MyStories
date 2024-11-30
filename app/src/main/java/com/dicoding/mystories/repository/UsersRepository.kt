package com.dicoding.mystories.repository

import com.dicoding.mystories.data.response.LoginResponse
import com.dicoding.mystories.data.response.RegisterResponse
import com.dicoding.mystories.data.retrofit.ApiService
import retrofit2.HttpException
import retrofit2.awaitResponse

class UsersRepository (
    private val apiService: ApiService
) {
    suspend fun postRegister(name: String, email: String, password: String): RegisterResponse? {
        val response = apiService.register(name, email, password).awaitResponse()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw HttpException(response)
        }
    }

    suspend fun postLogin(email: String, password: String): LoginResponse? {
        val response = apiService.login(email, password).awaitResponse()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw HttpException(response)
        }
    }
}