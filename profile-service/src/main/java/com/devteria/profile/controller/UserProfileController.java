package com.devteria.profile.controller;

import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.devteria.profile.service.UserProfileService;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileService userProfileService;

  @PostMapping
  UserProfileResponse createProfile(@RequestBody UserProfileCreationRequest request) {
    return userProfileService.createUserProfile(request);
  }

  @GetMapping("/{userProfileId}")
  UserProfileResponse getUserProfile(@PathVariable String userProfileId) {
    return userProfileService.getUserProfile(userProfileId);
  }

  @GetMapping("/internal/{userId}")
  UserProfileResponse getUserProfileByUserId(@PathVariable String userId) {
    return userProfileService.getUserProfileByUserId(userId);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  List<UserProfileResponse> getAllUserProfile() {
    return userProfileService.getAllUserProfile();
  }

}
