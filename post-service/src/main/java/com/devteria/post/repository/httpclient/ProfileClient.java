package com.devteria.post.repository.httpclient;

import com.devteria.post.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.profile.url}")
public interface ProfileClient {

  @GetMapping("/users/internal/{userId}")
  UserProfileResponse getUserProfileByUserId(@PathVariable String userId);
}
