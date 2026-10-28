package com.bhavesh.orbit.postsService.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PostLiked {

    private Long postId;
    private Long ownerUserId;
    private Long likedByUserId;


}
