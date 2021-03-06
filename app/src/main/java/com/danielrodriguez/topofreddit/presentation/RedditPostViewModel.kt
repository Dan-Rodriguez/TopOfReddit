package com.danielrodriguez.topofreddit.presentation

import android.app.Application
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import javax.inject.Inject

class RedditPostViewModel @Inject constructor(
    val post: RedditPost
) {
    val id: String = post.id
    val thumbnailPlaceholder: Int = android.R.drawable.ic_menu_camera
    val thumbnail = post.thumbnail
    val viewedAlpha = if (post.viewed) 0f else 1f
    val title = post.title
    val author = post.author
    val timeStamp: QuantityStringResource<Long>
        get() {
            val quantity = if (post.hoursAgo == 1L) 1 else 0

            return QuantityStringResource(R.plurals.x_hours_ago, quantity, post.hoursAgo)
        }
    val numberOfCommentsResource: QuantityStringResource<Int>
        get() {
            return QuantityStringResource(R.plurals.x_comments, post.numberOfComments, post.numberOfComments)
        }
}