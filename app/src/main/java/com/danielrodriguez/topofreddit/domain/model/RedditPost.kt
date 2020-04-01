package com.danielrodriguez.topofreddit.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RedditPost(val id: Int, val content: String, val details: String): Parcelable