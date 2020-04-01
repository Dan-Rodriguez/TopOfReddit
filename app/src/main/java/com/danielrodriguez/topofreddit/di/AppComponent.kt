package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.presentation.ItemListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ExecutorModule::class,
    UseCaseModule::class,
    ViewModelModule::class
])
interface AppComponent {

    fun inject(fragment: ItemListFragment)
}