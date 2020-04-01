package com.danielrodriguez.topofreddit.domain.usecase

import com.danielrodriguez.topofreddit.domain.model.RedditPost
import io.reactivex.rxjava3.core.Single

interface IGetRedditTopPostsUseCase {
    fun invoke(afterPost: RedditPost? = null): Single<List<RedditPost>>
}