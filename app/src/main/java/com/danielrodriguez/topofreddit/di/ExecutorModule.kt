package com.danielrodriguez.topofreddit.di

import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class ExecutorModule {

    @Provides
    fun providesBackgroundExecutorPoolSize() = 5

    @Singleton
    @Provides
    fun providesBackgroundExecutor(poolSize: Int): Executor = Executors.newFixedThreadPool(poolSize)
}