package com.danielrodriguez.topofreddit

import com.danielrodriguez.topofreddit.domain.model.RedditPost
import org.junit.Assert
import org.junit.Test
import java.util.*

class TestRedditPost {

    @Test
    fun testZeroHoursAgo() {

        val now = Date()

        val redditPost = createPostWithDate(Date(now.time))

        Assert.assertEquals(0, redditPost.hoursAgo)
    }

    private fun createPostWithDate(date: Date): RedditPost {
        return RedditPost(
            "2hqlxp",
            "t3",
            "Man trying to return a dog's toy gets tricked into playing fetch",
            "washedupwornout",
            date,
            "http://b.thumbs.redditmedia.com/9N1f7UGKM5fPZydrsgbb4_SUyyLW7A27um1VOygY3LM.jpg",
            958,
            false
        )
    }

    @Test
    fun testSomeHoursAgo() {

        val now = Date()
        val anHour = 1000 * 60 * 60

        val redditPost = createPostWithDate(Date(now.time - anHour))

        Assert.assertEquals(1, redditPost.hoursAgo)
    }
}
