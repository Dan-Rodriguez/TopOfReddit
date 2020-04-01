package com.danielrodriguez.topofreddit.data.repository

import android.os.Handler
import android.os.Looper
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.danielrodriguez.topofreddit.domain.repository.IRedditPostRepository
import io.reactivex.rxjava3.core.Single

class RedditPostRepository: IRedditPostRepository {

    private val PAGE_SIZE = 25

    override fun topPosts(): Single<List<RedditPost>> {
        return Single.create {
            Thread {
                Thread.sleep(2000)

                val posts = mutableListOf<RedditPost>()

                Handler(Looper.getMainLooper()).post {

                    for (i in 0..PAGE_SIZE) {
                        posts.add(createDummyItem(i))
                    }

                    it.onSuccess(posts)
                }
            }
                .start()
        }
    }

    override fun topPostsAfter(post: RedditPost): Single<List<RedditPost>> {
        return Single.create {
            Thread {
                Thread.sleep(2000)

                val posts = mutableListOf<RedditPost>()

                Handler(Looper.getMainLooper()).post {

                    val start = post.id
                    val end = start + PAGE_SIZE

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