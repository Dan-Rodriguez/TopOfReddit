package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.data.repository.datasource.IMapper
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostDto
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostMapper
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import dagger.Module
import dagger.Provides

@Module
class MapperModule {

    @Provides
    fun providesRedditPostMapper(): IMapper<RedditPostDto, RedditPost> = RedditPostMapper()
}