package com.danielrodriguez.topofreddit.data.repository.datasource.network

data class RedditPostDto(val id: String = "",
                         var kind: String = "",
                         val title: String = "",
                         val author: String = "",
                         val created: Long = 0,
                         val thumbnail: String = "",
                         val num_comments: Int = 0,
                         val visited: Boolean = false) // Unread status