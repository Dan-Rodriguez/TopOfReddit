package com.danielrodriguez.topofreddit.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danielrodriguez.topofreddit.MainThreadExecutor
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.danielrodriguez.topofreddit.domain.usecase.IGetRedditTopPostsUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor
import javax.inject.Inject

class ItemListViewModel @Inject constructor(
    private val getRedditTopPostsUseCase: IGetRedditTopPostsUseCase,
    private val backgroundExecutor: Executor
): ViewModel() {

    val postsCount: Int get() = _posts.value?.size ?: 0

    private val _error = MutableLiveData<Boolean>(false)
    val error: LiveData<Boolean> = _error

    val hasMore: Boolean get() = (_posts.value?.size ?: 0) < 50

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _posts = MutableLiveData<List<RedditPost>>()
    val posts: LiveData<List<RedditPost>> = _posts

    val title: Int = R.string.app_name

    private val compositeDisposable = CompositeDisposable()

    init {
        load()
    }

    private fun load() {
        _isLoading.value = true

        getRedditTopPostsUseCase.invoke()
            .observeOn(Schedulers.from(MainThreadExecutor()))
            .subscribeOn(Schedulers.from(backgroundExecutor))
            .subscribe({
                _posts.value = it
                _isLoading.value = false
            }, {
                _error.value = false
                _isLoading.value = false
            })
            .addTo(compositeDisposable)
    }

    fun loadMore() {
        _isLoading.value = true

        getRedditTopPostsUseCase.invoke(_posts.value?.last())
            .observeOn(Schedulers.from(MainThreadExecutor()))
            .subscribeOn(Schedulers.from(backgroundExecutor))
            .subscribe({ posts ->
                _posts.value = _posts.value?.toMutableList().also { it?.addAll(posts) }
                _isLoading.value = false
            }, {
                _error.value = false
                _isLoading.value = false
            })
            .addTo(compositeDisposable)
    }

    fun refresh() {
        load()
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    fun getPostAt(position: Int): RedditPost? {

        if (!validPosition(position)) {
            return null
        }

        return _posts.value?.get(position)
    }

    private fun validPosition(position: Int): Boolean {
        return (0 <= position && position < _posts.value?.size ?: 0)
    }

    fun removePostAt(position: Int) {
        if (!validPosition(position)) {
            return
        }

        _posts.value?.toMutableList()?.let {
            it.removeAt(position)
            _posts.value = it
        }
    }

    fun viewAt(position: Int) {
        if(!validPosition(position)) {
            return
        }

        _posts.value?.let {
            it[position].viewed = true
            _posts.value = it
        }
    }
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}