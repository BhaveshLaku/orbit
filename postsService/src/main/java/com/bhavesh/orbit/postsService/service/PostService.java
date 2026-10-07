package com.bhavesh.orbit.postsService.service;

import com.bhavesh.orbit.postsService.auth.AuthContextHolder;
import com.bhavesh.orbit.postsService.client.ConnectionServiceFeignClient;
import com.bhavesh.orbit.postsService.dto.PersonDto;
import com.bhavesh.orbit.postsService.dto.PostCreateRequestDto;
import com.bhavesh.orbit.postsService.dto.PostDto;
import com.bhavesh.orbit.postsService.entity.Post;
import com.bhavesh.orbit.postsService.exception.ResourceNotFoundException;
import com.bhavesh.orbit.postsService.mapper.PostMapper;
import com.bhavesh.orbit.postsService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final ConnectionServiceFeignClient connectionServiceFeignClient;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        log.info("Creating post for user with id: {}", userId);
        Post post = postMapper.toEntity(postCreateRequestDto);
        post.setUserId(userId);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting the post with ID: {}", postId);

        Long userId = AuthContextHolder.getCurrentUserId();

        //TODO: Remove in future
//        Call the Connection-service from the Posts Service and pass the UserId inside the Headers

        List<PersonDto> personDtoList = connectionServiceFeignClient.getFirstDegreeConnections(userId);

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
        return postMapper.toDto(post);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.info("Getting all posts of user with ID: {}", userId);
        List<Post> postList = postRepository.findAllByUserId(userId);

        return postList
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }
}
