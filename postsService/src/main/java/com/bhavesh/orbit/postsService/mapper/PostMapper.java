package com.bhavesh.orbit.postsService.mapper;

import com.bhavesh.orbit.postsService.dto.PostCreateRequestDto;
import com.bhavesh.orbit.postsService.dto.PostDto;
import com.bhavesh.orbit.postsService.entity.Post;
import org.springframework.stereotype.Component;

/** Explicit conversions between post entities and API DTOs. */
@Component
public class PostMapper {

    public Post toEntity(PostCreateRequestDto request) {
        Post post = new Post();
        post.setContent(request.getContent());
        return post;
    }

    public PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setUserId(post.getUserId());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}
