package com.danielrodriguez.topofreddit.data.repository.datasource.network

import com.danielrodriguez.topofreddit.data.repository.datasource.IMapper
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import java.util.*

class RedditPostMapper: IMapper<RedditPostDto, RedditPost> {

    override fun convertToEntity(dto: RedditPostDto): RedditPost {
        return RedditPost(
            dto.id,
            dto.kind,
            dto.title,
            dto.author,
            Date(dto.created * 1000), // To millis
            dto.thumbnail,
            dto.num_comments,
            dto.visited
        )
    }

    override fun convertToDto(entity: RedditPost): RedditPostDto {
        return RedditPostDto(
            entity.id,
            entity.kind,
            entity.title,
            entity.author,
            entity.created.time / 1000L, // To seconds
            entity.thumbnail,
            entity.numberOfComments,
            entity.viewed
        )
    }
}