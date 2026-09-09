package com.bhavesh.linkedInProject.postsService.service;

import com.bhavesh.linkedInProject.postsService.auth.AuthContextHolder;
import com.bhavesh.linkedInProject.postsService.dto.PostCreateRequestDto;
import com.bhavesh.linkedInProject.postsService.dto.PostDto;
import com.bhavesh.linkedInProject.postsService.entity.Post;
import com.bhavesh.linkedInProject.postsService.exception.ResourceNotFoundException;
import com.bhavesh.linkedInProject.postsService.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        log.info("Creating post for user with id: {}", userId);        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting the post with ID: {}", postId);

        Long userId = AuthContextHolder.getCurrentUserId();

        //TODO: Remove in future
//        Call the Connection-service from the Posts Service and pass the UserId inside the Headers

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.info("Getting all posts of user with ID: {}", userId);
        List<Post> postList = postRepository.findAllByUserId(userId);

        return postList
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}
