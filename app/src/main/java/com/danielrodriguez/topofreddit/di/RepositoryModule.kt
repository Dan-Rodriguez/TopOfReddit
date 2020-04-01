package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.data.repository.RedditPostRepository
import com.danielrodriguez.topofreddit.data.repository.datasource.IMapper
import com.danielrodriguez.topofreddit.data.repository.datasource.IRedditPostDataSource
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostDto
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.danielrodriguez.topofreddit.domain.repository.IRedditPostRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun providesRedditPostRepository(dataSource: IRedditPostDataSource, mapper: IMapper<RedditPostDto, RedditPost>): IRedditPostRepository
            = RedditPostRepository(dataSource, mapper)
}