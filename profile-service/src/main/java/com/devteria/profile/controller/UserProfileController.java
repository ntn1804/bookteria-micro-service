package com.devteria.profile.controller;

import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.devteria.profile.service.UserProfileService;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileService userProfileService;

  @PostMapping("users")
  UserProfileResponse createProfile(@RequestBody UserProfileCreationRequest request) {
    return userProfileService.createUserProfile(request);
  }

}