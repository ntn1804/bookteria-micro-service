package com.devteria.post.service;

import com.devteria.post.dto.PageResponse;
import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.dto.response.UserProfileResponse;
import com.devteria.post.entity.Post;
import com.devteria.post.mapper.PostMapper;
import com.devteria.post.repository.PostRepository;
import com.devteria.post.repository.httpclient.ProfileClient;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

  PostMapper postMapper;
  PostRepository postRepository;
  DateTimeFormatter dateTimeFormatter;
  ProfileClient profileClient;

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

  public PageResponse<PostResponse> getListPost(int pageNum, int pageSize) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Sort sort = Sort.by("createdDate").descending();
    Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

    Page<Post> pageData = postRepository.findAllByUserId(userId, pageable);

    UserProfileResponse userProfileResponse = null;

    try {
      userProfileResponse = profileClient.getUserProfileByUserId(userId);
    } catch (Exception exception) {
      log.info("Error while getting user profile", exception);
    }

    String username = userProfileResponse != null ? userProfileResponse.getFirstName() : null;

    List<PostResponse> postResponseList = pageData.getContent().stream().map(post -> {
      PostResponse postResponse = postMapper.toPostResponse(post);
      postResponse.setFormattedCreatedDate(dateTimeFormatter.format(post.getCreatedDate()));
      postResponse.setUsername(username);
      return postResponse;
    }).toList();

    return PageResponse.<PostResponse>builder()
        .currentPage(pageNum)
        .pageSize(pageSize)
        .totalElements(pageData.getTotalElements())
        .totalPage(pageData.getTotalPages())
        .data(postResponseList)
        .build();
  }
}
