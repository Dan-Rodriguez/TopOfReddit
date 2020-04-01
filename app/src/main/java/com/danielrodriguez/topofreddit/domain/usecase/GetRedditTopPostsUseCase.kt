package com.danielrodriguez.topofreddit.domain.usecase

import android.os.Handler
import android.os.Looper
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import io.reactivex.rxjava3.core.Single

class GetRedditTopPostsUseCase: IGetRedditTopPostsUseCase {
    override fun invoke(afterPost: RedditPost?): Single<List<RedditPost>> {
        return Single.create {
            Thread {
                Thread.sleep(2000)

                val posts = mutableListOf<RedditPost>()

                Handler(Looper.getMainLooper()).post {

                    val start = afterPost?.id ?: 0
                    val end = start + 25

                    for (i in start..end) {
                        posts.add(createDummyItem(i))
                    }

                    it.onSuccess(posts)
                }
            }
            .start()
        }
    }

    private fun createDummyItem(position: Int): RedditPost {
        return RedditPost(
            position,
            "Item " + position,
            makeDetails(position)
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        builder.append("\nMore details information here. $position")
        return builder.toString()
    }
}