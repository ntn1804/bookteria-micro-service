package com.devteria.profile.controller;

import com.devteria.profile.dto.request.UpdateProfileRequest;
import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.ApiResponse;
import com.devteria.profile.dto.response.UserProfileResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.devteria.profile.service.UserProfileService;
import org.springframework.web.multipart.MultipartFile;


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

  @GetMapping("/my-profile")
  ApiResponse<UserProfileResponse> getMyProfile() {
    return ApiResponse.<UserProfileResponse>builder()
        .result(userProfileService.getMyProfile())
        .build();
  }

  @PutMapping("/my-profile")
  ApiResponse<UserProfileResponse> updateMyProfile(@RequestBody UpdateProfileRequest request) {
    return ApiResponse.<UserProfileResponse>builder()
        .result(userProfileService.updateMyProfile(request))
        .build();
  }

  @PutMapping("/avatar")
  ApiResponse<UserProfileResponse> uploadAvatar(@RequestParam("file") MultipartFile file) {
    return ApiResponse.<UserProfileResponse>builder()
        .result(userProfileService.updateAvatar(file))
        .build();
  }
}
