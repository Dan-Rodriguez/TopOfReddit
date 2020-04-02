package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ServiceModule {

    private val baseUrl = "https://www.reddit.com/"

    @Provides
    fun providesRedditService(): RedditService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RedditService::class.java)
}