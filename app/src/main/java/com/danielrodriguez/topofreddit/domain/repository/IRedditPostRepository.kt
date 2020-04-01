package com.danielrodriguez.topofreddit.domain.repository

import com.danielrodriguez.topofreddit.domain.model.RedditPost
import io.reactivex.rxjava3.core.Single

interface IRedditPostRepository {
    fun topPosts(): Single<List<RedditPost>>
    fun topPostsAfter(post: RedditPost): Single<List<RedditPost>>
}