package com.danielrodriguez.topofreddit.data.repository.datasource.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {
    @GET("top.json")
    fun top(@Query("limit") limit: Int = 25): Call<RedditTopPostsCollectionDto>

    @GET("top.json")
    fun topAfter(@Query("after") after: String, @Query("limit") limit: Int = 25): Call<RedditTopPostsCollectionDto>
}