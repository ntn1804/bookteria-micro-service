package com.devteria.profile.service;

import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.UserProfileRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserProfileRepository userProfileRepository;
  private final UserProfileMapper userProfileMapper;

  public UserProfileResponse createUserProfile(UserProfileCreationRequest request) {
    UserProfile userProfile = userProfileMapper.toUserProfile(request);
    userProfileRepository.save(userProfile);

    return userProfileMapper.toUserProfileResponse(userProfile);
  }

  public UserProfileResponse getUserProfile(String userId) {
    UserProfile userProfile = userProfileRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User Profile not found"));

    return userProfileMapper.toUserProfileResponse(userProfile);
  }

  public List<UserProfileResponse> getAllUserProfile() {
    List<UserProfile> userProfiles = userProfileRepository.findAll();

    return userProfiles.stream().map(userProfileMapper::toUserProfileResponse).toList();

  }
}
