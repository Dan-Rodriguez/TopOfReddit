package com.danielrodriguez.topofreddit

import com.danielrodriguez.topofreddit.data.repository.RedditPostRepository
import com.danielrodriguez.topofreddit.data.repository.datasource.IRedditPostDataSource
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostDto
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostMapper
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditTopPostsCollectionDto
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

class TestRedditPostRepository {

    @Test
    fun testTopPostsEmpty() {
        val mapper = RedditPostMapper()
        val dataSource = mock(IRedditPostDataSource::class.java)
        `when`(dataSource.topPosts()).thenAnswer { Single.just(listOf<RedditPostDto>()) }

        val repository = RedditPostRepository(dataSource, mapper)

        val testScheduler = TestScheduler()
        val testObserver = repository.topPosts()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { it.count() == 0 }
        testObserver.assertNoErrors()

        testObserver.dispose()
    }

    @Test
    fun testTopPostsSuccessful() {
        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)
        val list = collection.data.children.map { it.data }

        val mapper = RedditPostMapper()
        val dataSource = mock(IRedditPostDataSource::class.java)
        `when`(dataSource.topPosts()).thenAnswer { Single.just(list) }

        val repository = RedditPostRepository(dataSource, mapper)

        val testScheduler = TestScheduler()
        val testObserver = repository.topPosts()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { it == list.map { mapper.convertToEntity(it) } }
        testObserver.assertNoErrors()

        testObserver.dispose()
    }

    @Test
    fun testTopPostsError() {
        val mapper = RedditPostMapper()
        val dataSource = mock(IRedditPostDataSource::class.java)
        `when`(dataSource.topPosts()).thenAnswer { Single.error<RedditPostDto>(IllegalArgumentException()) }

        val repository = RedditPostRepository(dataSource, mapper)

        val testScheduler = TestScheduler()
        val testObserver = repository.topPosts()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError { it is IllegalArgumentException }

        testObserver.dispose()
    }

    @Test
    fun testTopPostsAfterEmpty() {
        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)
        val list = collection.data.children.map { it.data }
        val last = list.last()

        val mapper = RedditPostMapper()
        val dataSource = mock(IRedditPostDataSource::class.java)
        `when`(dataSource.topPostsAfter(last)).thenAnswer { Single.just(listOf<RedditPostDto>()) }

        val repository = RedditPostRepository(dataSource, mapper)

        val testScheduler = TestScheduler()
        val testObserver = repository.topPostsAfter(mapper.convertToEntity(last))
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { it.count() == 0 }
        testObserver.assertNoErrors()

        testObserver.dispose()
    }

    @Test
    fun testTopPostsAfterSuccessful() {
        val topCollection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)
        val topList = topCollection.data.children.map { it.data }
        val last = topList.last()

        val afterCollection = Gson().fromJson(JsonReaderFromFile.readFromRaw("after.json"), RedditTopPostsCollectionDto::class.java)
        val afterList = afterCollection.data.children.map { it.data }

        val mapper = RedditPostMapper()
        val dataSource = mock(IRedditPostDataSource::class.java)
        `when`(dataSource.topPostsAfter(last)).thenAnswer { Single.just(afterList) }

        val repository = RedditPostRepository(dataSource, mapper)

        val testScheduler = TestScheduler()
        val testObserver = repository.topPostsAfter(mapper.convertToEntity(last))
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { it == afterList.map { mapper.convertToEntity(it) } }
        testObserver.assertNoErrors()

        testObserver.dispose()
    }


    @Test
    fun testTopPostsAfterError() {
        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)
        val list = collection.data.children.map { it.data }
        val last = list.last()

        val mapper = RedditPostMapper()
        val dataSource = mock(IRedditPostDataSource::class.java)
        `when`(dataSource.topPostsAfter(last)).thenAnswer { Single.error<RedditPostDto>(IllegalArgumentException()) }

        val repository = RedditPostRepository(dataSource, mapper)

        val testScheduler = TestScheduler()
        val testObserver = repository.topPostsAfter(mapper.convertToEntity(last))
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError { it is IllegalArgumentException }

        testObserver.dispose()
    }
}