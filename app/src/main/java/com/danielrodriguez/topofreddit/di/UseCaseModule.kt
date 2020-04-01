package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.domain.usecase.GetRedditTopPostsUseCase
import com.danielrodriguez.topofreddit.domain.usecase.IGetRedditTopPostsUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun providesIGetRedditTopPostsUseCase(): IGetRedditTopPostsUseCase = GetRedditTopPostsUseCase()
}