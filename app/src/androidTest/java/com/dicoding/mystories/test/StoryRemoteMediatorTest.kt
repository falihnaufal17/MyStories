package com.dicoding.mystories.test

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.mystories.data.StoryRemoteMediator
import com.dicoding.mystories.data.database.StoryDatabase
import com.dicoding.mystories.data.response.AddStoryResponse
import com.dicoding.mystories.data.response.DetailStoryResponse
import com.dicoding.mystories.data.response.ListStoryItem
import com.dicoding.mystories.data.response.ListStoryResponse
import com.dicoding.mystories.data.response.LoginResponse
import com.dicoding.mystories.data.response.LoginResult
import com.dicoding.mystories.data.response.RegisterResponse
import com.dicoding.mystories.data.response.Story
import com.dicoding.mystories.data.retrofit.ApiService
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Timeout
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {
    private val mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi
        )
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override fun register(name: String, email: String, password: String): Call<RegisterResponse> {
        return createFakeCall(
            RegisterResponse(
                error = false,
                message = "User $name successfully registered"
            )
        )
    }

    override fun login(email: String, password: String): Call<LoginResponse> {
        return createFakeCall(
            LoginResponse(
                error = false,
                message = "Login successful",
                loginResult = LoginResult(
                    name = "Mock name",
                    token = "Mock Token",
                    userId = "123sss"
                )
            )
        )
    }

    override fun addStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): Call<AddStoryResponse> {
        return createFakeCall(
            AddStoryResponse(
                error = false,
                message = "Story added successfully"
            )
        )
    }

    override suspend fun getStories(page: Int?, size: Int?, location: Int): ListStoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                null,
                null,
                "name $i",
                "description $i",
                9.2,
                "ID $i",
                9.2
            )

            items.add(story)
        }

        val startIndex = (page?.minus(1) ?: 0) * (size ?: 10)
        val endIndex = (startIndex + (size ?: 10)).coerceAtMost(items.size)

        val paginatedItems = if (startIndex < items.size) {
            items.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        val response = ListStoryResponse(
            listStory = paginatedItems,
            error = false,
            message = "Success"
        )

        return response
    }

    override suspend fun getStoriesNew(page: Int?, size: Int?, location: Int): ListStoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                null,
                null,
                "name $i",
                "description $i",
                9.2,
                "ID $i",
                9.2
            )

            items.add(story)
        }

        val startIndex = (page?.minus(1) ?: 0) * (size ?: 10)
        val endIndex = (startIndex + (size ?: 10)).coerceAtMost(items.size)

        val paginatedItems = if (startIndex < items.size) {
            items.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        val response = ListStoryResponse(
            listStory = paginatedItems,
            error = false,
            message = "Success"
        )

        return response
    }

    override fun getDetailStory(id: String): Call<DetailStoryResponse> {
        return createFakeCall(
            DetailStoryResponse(
                error = false,
                message = "Story details fetched successfully",
                story = Story(
                    id = id,
                    name = "Fake Story",
                    description = "This is a detailed description of the story.",
                    photoUrl = "https://example.com/detail_story.jpg",
                    createdAt = "2024-01-01T00:00:00Z",
                    lat = 9.2,
                    lon = 9.2
                )
            )
        )
    }

    private fun <T> createFakeCall(response: T): Call<T> {
        return object : Call<T> {
            override fun enqueue(callback: retrofit2.Callback<T>) {
                callback.onResponse(this, retrofit2.Response.success(response))
            }

            override fun isExecuted() = false
            override fun clone() = this
            override fun isCanceled() = false
            override fun cancel() {}
            override fun execute(): retrofit2.Response<T> = retrofit2.Response.success(response)
            override fun request() = okhttp3.Request.Builder().url("https://example.com").build()
            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
    }
}