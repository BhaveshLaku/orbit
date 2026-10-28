package com.bhavesh.orbit.postsService.event;

import com.bhavesh.orbit.postsService.entity.Post;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class PostLiked {

    private Long postId;
    private Long ownerUserId;
    private Long likedByUserId;


}
