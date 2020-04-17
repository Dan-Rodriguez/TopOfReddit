package com.danielrodriguez.topofreddit

import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostNetworkDataSource
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditService
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditTopPostsCollectionDto
import com.google.gson.Gson
import io.reactivex.rxjava3.schedulers.TestScheduler
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response
import retrofit2.mock.Calls
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit


class TestRedditPostNetworkDataSource {

    private val emptyRedditTopPostsCollectionJson
    get() = "{" +
            "  \"kind\": \"Listing\"," +
            "  \"data\": {" +
            "    \"modhash\": \"uo8ez9e1lf807810187b428b01e59cd38a9303245aa595d992\"," +
            "    \"children\": []," +
            "    \"after\": \"t3_2hpw7k\"," +
            "    \"before\": null" +
            "  }" +
            "}"

    @Test
    fun testTopPostsSuccessful() {

        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)

        val mockService = mock(RedditService::class.java)
        `when`(mockService.top()).thenAnswer { Calls.response(collection) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPosts()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { 25 == it.count() }
        testObserver.assertValue { it == collection.data.children.map { it.data } }
        testObserver.assertNoErrors()
        testObserver.dispose()

        verify(mockService, times(1)).top()
    }

    @Test
    fun testTopPostsEmpty() {

        val collection = Gson().fromJson(emptyRedditTopPostsCollectionJson, RedditTopPostsCollectionDto::class.java)

        val mockService = mock(RedditService::class.java)
        `when`(mockService.top()).thenAnswer { Calls.response(collection) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPosts()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { 0 == it.count() }
        testObserver.assertNoErrors()
        testObserver.dispose()

        verify(mockService, times(1)).top()
    }

    @Test
    fun testTopPostsError() {

        val mockService = mock(RedditService::class.java)
        `when`(mockService.top()).thenAnswer { Calls.failure<Throwable>(IllegalArgumentException()) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPosts()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError { true }
        testObserver.dispose()

        verify(mockService, times(1)).top()
    }

    @Test
    fun testTopPostsUnsuccessful() {

        val mockService = mock(RedditService::class.java)
        `when`(mockService.top()).thenAnswer { Calls.response(Response.error<String>(500, ResponseBody.create(null, "Error"))) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPosts()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError {
            Assert.assertTrue(it is Exception)
            Assert.assertEquals("Error", it.localizedMessage)
            true
        }
        testObserver.dispose()

        verify(mockService, times(1)).top()
    }

    @Test
    fun testTopPostsNullResponse() {

        val mockService = mock(RedditService::class.java)
        `when`(mockService.top()).thenAnswer { Calls.response(Response.success(null)) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPosts()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError { it is NoSuchElementException }
        testObserver.dispose()

        verify(mockService, times(1)).top()
    }

    @Test
    fun testTopAfterSuccessful() {

        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)

        val last = collection.data.children.last()
        // This is set in the callback of the DataSource, so it is not set yet
        last.data.kind = last.kind
        val id = "${last.kind}_${last.data.id}"

        val mockService = mock(RedditService::class.java)
        `when`(mockService.topAfter(id)).thenAnswer { Calls.response(collection) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPostsAfter(last.data)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { 25 == it.count() }
        testObserver.assertValue { it == collection.data.children.map { it.data } }
        testObserver.assertNoErrors()
        testObserver.dispose()

        verify(mockService, times(1)).topAfter(id)
    }

    @Test
    fun testTopAfterEmpty() {

        val topCollection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)

        val last = topCollection.data.children.last()
        // This is set in the callback of the DataSource, so it is not set yet
        last.data.kind = last.kind
        val id = "${last.kind}_${last.data.id}"

        val afterCollection = Gson().fromJson(emptyRedditTopPostsCollectionJson, RedditTopPostsCollectionDto::class.java)

        val mockService = mock(RedditService::class.java)
        `when`(mockService.topAfter(id)).thenAnswer { Calls.response(afterCollection) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPostsAfter(last.data)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { 0 == it.count() }
        testObserver.assertNoErrors()
        testObserver.dispose()

        verify(mockService, times(1)).topAfter(id)
    }

    @Test
    fun testTopAfterError() {

        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)

        val last = collection.data.children.last()
        // This is set in the callback of the DataSource, so it is not set yet
        last.data.kind = last.kind
        val id = "${last.kind}_${last.data.id}"

        val mockService = mock(RedditService::class.java)
        `when`(mockService.topAfter(id)).thenAnswer { Calls.failure<Throwable>(IllegalArgumentException()) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPostsAfter(last.data)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError { true }
        testObserver.dispose()

        verify(mockService, times(1)).topAfter(id)
    }

    @Test
    fun testTopAfterUnsuccessful() {

        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)

        val last = collection.data.children.last()
        // This is set in the callback of the DataSource, so it is not set yet
        last.data.kind = last.kind
        val id = "${last.kind}_${last.data.id}"

        val mockService = mock(RedditService::class.java)
        `when`(mockService.topAfter(id)).thenAnswer { Calls.response(Response.error<String>(500, ResponseBody.create(null, "Error"))) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPostsAfter(last.data)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError {
            Assert.assertTrue(it is Exception)
            Assert.assertEquals("Error", it.localizedMessage)
            true
        }
        testObserver.dispose()

        verify(mockService, times(1)).topAfter(id)
    }

    @Test
    fun testTopAfterNullResponse() {

        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)

        val last = collection.data.children.last()
        // This is set in the callback of the DataSource, so it is not set yet
        last.data.kind = last.kind
        val id = "${last.kind}_${last.data.id}"

        val mockService = mock(RedditService::class.java)
        `when`(mockService.topAfter(id)).thenAnswer { Calls.response(Response.success(null)) }

        val dataSource = RedditPostNetworkDataSource(mockService)

        val testScheduler = TestScheduler()

        val testObserver = dataSource.topPostsAfter(last.data)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError { it is NoSuchElementException }
        testObserver.dispose()

        verify(mockService, times(1)).topAfter(id)
    }
}