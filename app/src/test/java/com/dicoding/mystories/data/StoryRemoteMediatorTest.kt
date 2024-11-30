package com.dicoding.mystories.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.mystories.data.database.StoryDatabase
import com.dicoding.mystories.data.response.AddStoryResponse
import com.dicoding.mystories.data.response.DetailStoryResponse
import com.dicoding.mystories.data.response.ListStoryItem
import com.dicoding.mystories.data.response.ListStoryResponse
import com.dicoding.mystories.data.response.LoginResponse
import com.dicoding.mystories.data.response.RegisterResponse
import com.dicoding.mystories.data.retrofit.ApiService
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okio.Timeout
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        TODO("Not yet implemented")
    }

    override fun login(email: String, password: String): Call<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun addStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): Call<AddStoryResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(page: Int?, size: Int?, location: Int): Call<ListStoryResponse> {
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

        return object : Call<ListStoryResponse> {
            override fun enqueue(callback: Callback<ListStoryResponse>) {
                callback.onResponse(this, Response.success(response))
            }

            override fun isExecuted(): Boolean = false
            override fun clone(): Call<ListStoryResponse> = this
            override fun isCanceled(): Boolean = false
            override fun cancel() {}
            override fun execute(): Response<ListStoryResponse> = Response.success(response)
            override fun request(): Request = Request.Builder().build()
            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
    }

    override fun getDetailStory(id: String): Call<DetailStoryResponse> {
        TODO("Not yet implemented")
    }
}