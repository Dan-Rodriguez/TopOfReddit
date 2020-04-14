package com.danielrodriguez.topofreddit

import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostDto
import com.danielrodriguez.topofreddit.data.repository.datasource.network.RedditPostMapper
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import org.junit.Assert
import org.junit.Test
import java.util.*

class TestRedditPostMapper {

    @Test
    fun testMapToDto() {

        val now = Date()
        val post = RedditPost(
            "2hqlxp",
            "t3",
            "Man trying to return a dog's toy gets tricked into playing fetch",
            "washedupwornout",
            now,
            "http://b.thumbs.redditmedia.com/9N1f7UGKM5fPZydrsgbb4_SUyyLW7A27um1VOygY3LM.jpg",
            958,
            false
        )

        val mapper = RedditPostMapper()
        val postDto = mapper.convertToDto(post)

        Assert.assertEquals(post.id, postDto.id)
        Assert.assertEquals(post.kind, postDto.kind)
        Assert.assertEquals(post.title, postDto.title)
        Assert.assertEquals(post.author, postDto.author)
        Assert.assertEquals(post.created.time / 1000, postDto.created)
        Assert.assertEquals(post.thumbnail, postDto.thumbnail)
        Assert.assertEquals(post.numberOfComments, postDto.num_comments)
        Assert.assertEquals(post.viewed, postDto.visited)
    }

    @Test
    fun testMapToEntity() {

        val now = Date()
        val postDto = RedditPostDto(
            "2hqlxp",
            "t3",
            "Man trying to return a dog's toy gets tricked into playing fetch",
            "washedupwornout",
            now.time,
            "http://b.thumbs.redditmedia.com/9N1f7UGKM5fPZydrsgbb4_SUyyLW7A27um1VOygY3LM.jpg",
            958,
            false
        )

        val mapper = RedditPostMapper()
        val post = mapper.convertToEntity(postDto)

        Assert.assertEquals(post.id, post.id)
        Assert.assertEquals(postDto.kind, post.kind)
        Assert.assertEquals(postDto.title, post.title)
        Assert.assertEquals(postDto.author, post.author)
        Assert.assertEquals(postDto.created, post.created.time / 1000)
        Assert.assertEquals(postDto.thumbnail, post.thumbnail)
        Assert.assertEquals(postDto.num_comments, post.numberOfComments)
        Assert.assertEquals(postDto.visited, post.viewed)
    }
}