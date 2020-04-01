package com.danielrodriguez.topofreddit.di

import com.danielrodriguez.topofreddit.presentation.ItemListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelModule::class
])
interface AppComponent {

    fun inject(fragment: ItemListFragment)
}