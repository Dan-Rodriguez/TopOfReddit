package com.danielrodriguez.topofreddit.data.repository.datasource.network

import com.danielrodriguez.topofreddit.data.repository.datasource.IRedditPostDataSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RedditPostNetworkDataSource @Inject constructor(
    private val service: RedditService
): IRedditPostDataSource {

    private val PAGE_SIZE = 25

    private fun callback(emitter: SingleEmitter<List<RedditPostDto>>)
    = object: Callback<RedditTopPostsCollectionDto> {
        override fun onFailure(call: Call<RedditTopPostsCollectionDto>, t: Throwable) {
            emitter.onError(t)
        }

        override fun onResponse(
            call: Call<RedditTopPostsCollectionDto>,
            response: Response<RedditTopPostsCollectionDto>
        ) {
            if (response.isSuccessful) {
                response.body()?.data?.children?.map { postDtoData ->
                    postDtoData.data.also { it.kind = postDtoData.kind }
                }?.let {
                    emitter.onSuccess(it)
                } ?: emitter.onError(NoSuchElementException())

            } else {
                emitter.onError(Exception(response.errorBody()?.string()))
            }
        }
    }

    override fun topPosts(): Single<List<RedditPostDto>> {
        return Single.create {
            service.top(PAGE_SIZE).enqueue(callback(it))
        }
    }

    override fun topPostsAfter(post: RedditPostDto): Single<List<RedditPostDto>> {
        return Single.create {
            val key = "${post.kind}_${post.id}"
            service.topAfter(key, PAGE_SIZE).enqueue(callback(it))
        }
    }
}