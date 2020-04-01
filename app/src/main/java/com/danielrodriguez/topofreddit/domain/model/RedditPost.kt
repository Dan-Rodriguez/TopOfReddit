package com.danielrodriguez.topofreddit.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class RedditPost(val id: String = "",
                      val kind: String = "",
                      val title: String = "",
                      val author: String = "",
                      val created: Date = Date(),
                      val thumbnail: String = "",
                      val numberOfComments: Int = 0,
                      var viewed: Boolean = false): Parcelable {
    val hoursAgo: Long
        get() = (Date().time - created.time).hours
}

val Long.secs: Long get() = this / 1000
val Long.mins: Long get() = this.secs / 60
val Long.hours: Long get() = this.mins / 60