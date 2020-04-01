package com.danielrodriguez.topofreddit

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.danielrodriguez.topofreddit.di.AppComponent
import com.danielrodriguez.topofreddit.di.DaggerAppComponent

class TopOfRedditApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // TODO - Just for loading dummy data from raw
        context = applicationContext
        appComponent = DaggerAppComponent.builder().build()
    }

    companion object {
        lateinit var context: Context
    }
}

val FragmentActivity.topOfRedditApplication get() = this.application as TopOfRedditApplication