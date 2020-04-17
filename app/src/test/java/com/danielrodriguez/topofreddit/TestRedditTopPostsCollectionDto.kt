package com.danielrodriguez.topofreddit

import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditTopPostsCollectionDto
import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test

class TestRedditTopPostsCollectionDto {

    @Test
    fun testProperlyParsed() {
        val collection = Gson().fromJson(JsonReaderFromFile.readFromRaw("top.json"), RedditTopPostsCollectionDto::class.java)
        Assert.assertEquals(25, collection.data.children.count())

        for (child in collection.data.children) {
            Assert.assertEquals("t3", child.kind)
        }
    }
}