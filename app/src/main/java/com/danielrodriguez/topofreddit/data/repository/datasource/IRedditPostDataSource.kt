package com.danielrodriguez.topofreddit.data.repository.datasource

import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostDto
import io.reactivex.rxjava3.core.Single

interface IRedditPostDataSource {
    fun topPosts(): Single<List<RedditPostDto>>
    fun topPostsAfter(post: RedditPostDto): Single<List<RedditPostDto>>
}