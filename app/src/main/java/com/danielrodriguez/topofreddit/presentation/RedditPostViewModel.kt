package com.danielrodriguez.topofreddit.presentation

import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.TopOfRedditApplication
import com.danielrodriguez.topofreddit.domain.model.RedditPost

class RedditPostViewModel(
    val post: RedditPost
) {
    private val context = TopOfRedditApplication.context

    val id: String = post.id
    val thumbnailPlaceholder: Int = android.R.drawable.ic_menu_camera
    val thumbnail = post.thumbnail
    val viewedAlpha = if (post.viewed) 0f else 1f
    val title = post.title
    val author = post.author
    val timeStamp: String
        get() {
            val quantity = if (post.hoursAgo == 1L) 1 else 0

            return context.resources.getQuantityString(R.plurals.x_hours_ago, quantity, post.hoursAgo)
        }
    val numberOfComments: String
        get() {
            return context.resources.getQuantityString(R.plurals.x_comments, post.numberOfComments, post.numberOfComments)
        }
}