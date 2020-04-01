package com.danielrodriguez.topofreddit.data.repository.datasource.network

data class RedditTopPostsCollectionDto(val data: RedditTopPostsCollectionDataDto)

data class RedditTopPostsCollectionDataDto(val children: List<RedditPostDtoData>)

data class RedditPostDtoData(val kind: String, val data: RedditPostDto)