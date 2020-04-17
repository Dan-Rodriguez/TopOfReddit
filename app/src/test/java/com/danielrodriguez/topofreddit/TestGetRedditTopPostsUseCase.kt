package com.danielrodriguez.topofreddit

import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostDto
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostMapper
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditTopPostsCollectionDto
import com.danielrodriguez.topofreddit.domain.repository.IRedditPostRepository
import com.danielrodriguez.topofreddit.domain.usecase.GetRedditTopPostsUseCase
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito.*
import java.util.concurrent.TimeUnit

class TestGetRedditTopPostsUseCase {

    /*
     * Tests that the top posts is empty and is being retrieved
     */
    @Test
    fun invokeNoArgumentsEmptyList() {

        val repository = mock(IRedditPostRepository::class.java)
        `when`(repository.topPosts()).thenAnswer { Single.just(listOf<RedditPostDto>()) }

        val useCase = GetRedditTopPostsUseCase(repository)

        val testScheduler = TestScheduler()
        val testObserver = useCase.invoke()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { 0 == it.count() }
        testObserver.assertNoErrors()
        testObserver.dispose()

        verify(repository).topPosts()
        verifyNoMoreInteractions(repository)
    }

    /*
     * Tests that the top posts is not empty and is being retrieved
     */
    @Test
    fun invokeNoArgumentsSuccess() {

        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)
        val list = collection.data.children.map { it.data }

        val repository = mock(IRedditPostRepository::class.java)
        `when`(repository.topPosts()).thenAnswer { Single.just(list) }

        val useCase = GetRedditTopPostsUseCase(repository)

        val testScheduler = TestScheduler()
        val testObserver = useCase.invoke()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { list == it }
        testObserver.assertNoErrors()
        testObserver.dispose()

        verify(repository).topPosts()
        verifyNoMoreInteractions(repository)
    }

    /*
     * Tests that the top posts fails at being retrieved
     */
    @Test
    fun invokeNoArgumentsError() {

        val repository = mock(IRedditPostRepository::class.java)
        `when`(repository.topPosts()).thenAnswer { Single.error<RedditPostDto>(IllegalArgumentException()) }

        val useCase = GetRedditTopPostsUseCase(repository)

        val testScheduler = TestScheduler()
        val testObserver = useCase.invoke()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError { it is java.lang.IllegalArgumentException }
        testObserver.dispose()

        verify(repository).topPosts()
        verifyNoMoreInteractions(repository)
    }

    /*
     * Tests that the after posts is empty and is being retrieved
     */
    @Test
    fun invokeArgumentsEmpty() {

        val topCollection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)
        val topList = topCollection.data.children.map { it.data }
        val topListLast = topList.last()
        val topListLastEntity = RedditPostMapper().convertToEntity(topListLast)

        val repository = mock(IRedditPostRepository::class.java)
        `when`(repository.topPostsAfter(topListLastEntity)).thenAnswer { Single.just(listOf<RedditPostDto>()) }

        val useCase = GetRedditTopPostsUseCase(repository)

        val testScheduler = TestScheduler()
        val testObserver = useCase.invoke(topListLastEntity)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { 0 == it.count() }
        testObserver.assertNoErrors()
        testObserver.dispose()

        verify(repository).topPostsAfter(topListLastEntity)
        verifyNoMoreInteractions(repository)
    }

    /*
     * Tests that the after posts is empty and is being retrieved
     */
    @Test
    fun invokeArgumentsSuccessful() {

        val topCollection = Gson().fromJson(JsonReaderFromFile.readFromRaw("after.json"), RedditTopPostsCollectionDto::class.java)
        val topListLast = topCollection.data.children.map { it.data }.last()
        val topListLastEntity = RedditPostMapper().convertToEntity(topListLast)

        val afterCollection = Gson().fromJson(JsonReaderFromFile.readFromRaw("after.json"), RedditTopPostsCollectionDto::class.java)
        val afterList = afterCollection.data.children.map { it.data }

        val repository = mock(IRedditPostRepository::class.java)
        `when`(repository.topPostsAfter(topListLastEntity)).thenAnswer { Single.just(afterList) }

        val useCase = GetRedditTopPostsUseCase(repository)

        val testScheduler = TestScheduler()
        val testObserver = useCase.invoke(topListLastEntity)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValue { afterList == it }
        testObserver.assertNoErrors()
        testObserver.dispose()

        verify(repository).topPostsAfter(topListLastEntity)
        verifyNoMoreInteractions(repository)
    }

    /*
     * Tests that the after posts fails at being retrieved
     */
    @Test
    fun invokeArgumentsError() {

        val topCollection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)
        val topListLast = topCollection.data.children.map { it.data }.last()
        val topListLastEntity = RedditPostMapper().convertToEntity(topListLast)

        val repository = mock(IRedditPostRepository::class.java)
        `when`(repository.topPostsAfter(topListLastEntity)).thenAnswer { Single.error<RedditPostDto>(IllegalArgumentException()) }

        val useCase = GetRedditTopPostsUseCase(repository)

        val testScheduler = TestScheduler()
        val testObserver = useCase.invoke(topListLastEntity)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertValueCount(0)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertError { it is java.lang.IllegalArgumentException }
        testObserver.dispose()

        verify(repository).topPostsAfter(topListLastEntity)
        verifyNoMoreInteractions(repository)
    }
}