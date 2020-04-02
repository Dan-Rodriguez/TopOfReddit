package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.presentation.ItemListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ExecutorModule::class,
    DataSourceModule::class,
    MapperModule::class,
    RepositoryModule::class,
    ServiceModule::class,
    UseCaseModule::class,
    ViewModelModule::class
])
interface AppComponent {

    fun inject(fragment: ItemListFragment)
}