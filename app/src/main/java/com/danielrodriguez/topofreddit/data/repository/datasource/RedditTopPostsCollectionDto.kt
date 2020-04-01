package com.danielrodriguez.topofreddit.data.repository.datasource

data class RedditTopPostsCollectionDto(val data: RedditTopPostsCollectionDataDto)

data class RedditTopPostsCollectionDataDto(val children: List<RedditPostDtoData>)

data class RedditPostDtoData(val kind: String, val data: RedditPostDto)

data class RedditPostDto(val id: String = "",
                         var kind: String = "",
                         val title: String = "",
                         val author: String = "",
                         val created: Long = 0,
                         val thumbnail: String = "",
                         val num_comments: Int = 0,
                         val visited: Boolean = false) // Unread status