package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.data.repository.RedditPostRepository
import com.danielrodriguez.topofreddit.domain.repository.IRedditPostRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun providesRedditPostRepository(): IRedditPostRepository = RedditPostRepository()
}