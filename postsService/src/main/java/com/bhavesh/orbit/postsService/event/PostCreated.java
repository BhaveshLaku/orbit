package com.bhavesh.orbit.postsService.event;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class PostCreated {

    private Long ownerUserId;
    private Long postId;
    private Long userId;
    private String content;

}
