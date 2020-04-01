package com.danielrodriguez.topofreddit

import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.danielrodriguez.topofreddit.di.AppComponent
import com.danielrodriguez.topofreddit.di.DaggerAppComponent

class TopOfRedditApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().build()
    }
}

val FragmentActivity.topOfRedditApplication get() = this.application as TopOfRedditApplication