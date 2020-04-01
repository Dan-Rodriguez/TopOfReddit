package com.danielrodriguez.topofreddit.data.repository.datasource.network

import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.TopOfRedditApplication
import com.danielrodriguez.topofreddit.data.repository.datasource.IRedditPostDataSource
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import java.io.IOException

class RedditPostNetworkDataSource: IRedditPostDataSource {

    private val PAGE_SIZE = 12

    private var posts: List<RedditPostDto>

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
        posts = collection.data.children.map { it.data }
    }

    override fun topPosts(): Single<List<RedditPostDto>> {
        return Single.create {
            val pagePosts = mutableListOf<RedditPostDto>()

            for (i in 0 until PAGE_SIZE) {
                pagePosts.add(posts[i])
            }

            it.onSuccess(pagePosts)
        }
    }

    override fun topPostsAfter(post: RedditPostDto): Single<List<RedditPostDto>> {
        return Single.create {
            val pagePosts = mutableListOf<RedditPostDto>()
            val start = posts.indexOf(post)
            val end = start + PAGE_SIZE

            for (i in start until end) {
                pagePosts.add(posts[i])
            }

            it.onSuccess(pagePosts)
        }
    }
}