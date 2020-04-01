package com.danielrodriguez.topofreddit.domain.usecase

import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.danielrodriguez.topofreddit.domain.repository.IRedditPostRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetRedditTopPostsUseCase @Inject constructor(
    private val repository: IRedditPostRepository
): IGetRedditTopPostsUseCase {

    override fun invoke(afterPost: RedditPost?): Single<List<RedditPost>> {
        return afterPost?.let {
                repository.topPostsAfter(it)

            } ?: run {
                repository.topPosts()
            }
    }
}