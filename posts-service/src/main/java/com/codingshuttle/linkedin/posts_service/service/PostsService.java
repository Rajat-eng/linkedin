package com.codingshuttle.linkedin.posts_service.service;

import com.codingshuttle.linkedin.posts_service.clients.ConnectionsClient;
import com.codingshuttle.linkedin.posts_service.dto.PostCreateRequestDto;
import com.codingshuttle.linkedin.posts_service.dto.PostDto;
import com.codingshuttle.linkedin.posts_service.entity.Post;
import com.codingshuttle.linkedin.posts_service.event.PostCreatedEvent;
import com.codingshuttle.linkedin.posts_service.exception.ResourceNotFoundException;
import com.codingshuttle.linkedin.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postDto) {
        String userId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(Long.valueOf(userId));

        Post savedPost = postsRepository.save(post);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(Long.valueOf(userId))
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send(KafkaTopicConfig.POST_CREATED_TOPIC, postCreatedEvent);

        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with ID: {}", postId);

        Post post = postsRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id: "+postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts = postsRepository.findByUserId(userId);

        return posts
            .stream()
            .map((element) -> modelMapper.map(element, PostDto.class))
            .collect(Collectors.toList());
    }
}
