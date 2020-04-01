package com.danielrodriguez.topofreddit.domain.usecase

import android.os.Handler
import android.os.Looper
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.danielrodriguez.topofreddit.domain.repository.IRedditPostRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetRedditTopPostsUseCase @Inject constructor(
    private val repository: IRedditPostRepository
): IGetRedditTopPostsUseCase {

    override fun invoke(afterPost: RedditPost?): Single<List<RedditPost>> {
        return Single.create {
            afterPost?.let {
                repository.topPostsAfter(it)

            } ?: run {
                repository.topPosts()
            }
        }
    }
}