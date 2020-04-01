package com.danielrodriguez.topofreddit.data.repository

import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.TopOfRedditApplication
import com.danielrodriguez.topofreddit.data.repository.datasource.RedditTopPostsCollectionDto
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.danielrodriguez.topofreddit.domain.repository.IRedditPostRepository
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import java.util.*

class RedditPostRepository: IRedditPostRepository {

    private val PAGE_SIZE = 12

    private var posts: List<RedditPost>

    init {
        val json = try {
            val inputStream = TopOfRedditApplication.context.resources.openRawResource(R.raw.top)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)

        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }

        val collection = Gson().fromJson<RedditTopPostsCollectionDto>(json, RedditTopPostsCollectionDto::class.java)
        posts = collection.data.children.map {
            RedditPost(
                it.data.id,
                it.data.kind,
                it.data.title,
                it.data.author,
                Date(it.data.created * 1000), // To millis
                it.data.thumbnail,
                it.data.num_comments,
                it.data.visited
            )
        }
    }

    override fun topPosts(): Single<List<RedditPost>> {
        return Single. create {
            val pagePosts = mutableListOf<RedditPost>()

            for (i in 0 until PAGE_SIZE) {
                pagePosts.add(posts[i])
            }

            it.onSuccess(pagePosts)
        }
    }

    override fun topPostsAfter(post: RedditPost): Single<List<RedditPost>> {
        return Single.create {
            val pagePosts = mutableListOf<RedditPost>()
            val start = posts.indexOf(post)
            val end = start + PAGE_SIZE

            for (i in start until end) {
                pagePosts.add(posts[i])
            }

            it.onSuccess(pagePosts)
        }
    }
}