package com.devteria.profile.service;

import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import com.devteria.profile.mapper.UserProfileMapper;
import org.springframework.stereotype.Service;
import com.devteria.profile.repository.UserProfileRepository;

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
}
