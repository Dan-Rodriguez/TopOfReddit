package com.danielrodriguez.topofreddit.di

import android.app.Application
import com.danielrodriguez.topofreddit.presentation.ItemListFragment
import dagger.BindsInstance
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
    fun inject(application: Application)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}