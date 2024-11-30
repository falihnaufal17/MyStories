package com.dicoding.mystories.data.retrofit

import com.dicoding.mystories.data.response.AddStoryResponse
import com.dicoding.mystories.data.response.DetailStoryResponse
import com.dicoding.mystories.data.response.ListStoryResponse
import com.dicoding.mystories.data.response.LoginResponse
import com.dicoding.mystories.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded()
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded()
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : Call<AddStoryResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int = 1
    ) : Call<ListStoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id: String
    ) : Call<DetailStoryResponse>
}