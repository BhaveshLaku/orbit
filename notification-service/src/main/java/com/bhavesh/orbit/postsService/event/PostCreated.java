package com.bhavesh.orbit.postsService.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PostCreated {

    private Long ownerUserId;
    private Long postId;
    private Long userId;
    private String content;

}
