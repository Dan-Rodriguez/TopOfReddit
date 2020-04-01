package com.danielrodriguez.topofreddit.data.repository

import com.danielrodriguez.topofreddit.data.repository.datasource.IMapper
import com.danielrodriguez.topofreddit.data.repository.datasource.IRedditPostDataSource
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostDto
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.danielrodriguez.topofreddit.domain.repository.IRedditPostRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RedditPostRepository @Inject constructor(
    private val dataSource: IRedditPostDataSource,
    private val mapper: IMapper<RedditPostDto, RedditPost>
): IRedditPostRepository {

    override fun topPosts(): Single<List<RedditPost>> {
        return dataSource.topPosts()
            .map {
                it.map { mapper.convertToEntity(it) }
            }
    }

    override fun topPostsAfter(post: RedditPost): Single<List<RedditPost>> {
        return dataSource.topPostsAfter(mapper.convertToDto(post))
            .map {
                it.map { mapper.convertToEntity(it) }
            }
    }
}