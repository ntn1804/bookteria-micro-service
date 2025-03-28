package com.devteria.post.controller;

import com.devteria.post.dto.ApiResponse;
import com.devteria.post.dto.PageResponse;
import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

  PostService postService;

  @PostMapping("/create")
  public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
    return ApiResponse.<PostResponse>builder()
        .message("SUCCESS")
        .result(postService.createPost(request))
        .build();
  }

  @GetMapping("/my-posts")
  public ApiResponse<PageResponse<PostResponse>> getListPost(
      @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
    return ApiResponse.<PageResponse<PostResponse>>builder()
        .message("SUCCESS")
        .result(postService.getListPost(pageNum, pageSize))
        .build();
  }

}
