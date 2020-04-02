package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.data.repository.datasource.IRedditPostDataSource
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostNetworkDataSource
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditService
import dagger.Module
import dagger.Provides

@Module
class DataSourceModule {

    @Provides
    fun providesRedditPostDataSource(service: RedditService): IRedditPostDataSource
            = RedditPostNetworkDataSource(service)
}