package com.devteria.post.service;

import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.entity.Post;
import com.devteria.post.mapper.PostMapper;
import com.devteria.post.repository.PostRepository;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

  PostMapper postMapper;
  PostRepository postRepository;

  public PostResponse createPost(PostRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Post post = Post.builder()
        .userId(userId)
        .content(request.getContent())
        .createdDate(Instant.now())
        .modifiedDate(Instant.now())
        .build();

    postRepository.save(post);

    return postMapper.toPostResponse(post);
  }

  public List<PostResponse> getListPost() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    List<Post> listPost = postRepository.findAllByUserId(userId);

    return listPost.stream().map(postMapper::toPostResponse).toList();
  }
}
