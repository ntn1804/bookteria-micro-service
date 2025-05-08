package com.devteria.profile.service;

import com.devteria.profile.dto.request.UpdateProfileRequest;
import com.devteria.profile.dto.request.UserProfileCreationRequest;
import com.devteria.profile.dto.response.ApiResponse;
import com.devteria.profile.dto.response.FileResponse;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import com.devteria.profile.exception.AppException;
import com.devteria.profile.exception.ErrorCode;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.UserProfileRepository;
import com.devteria.profile.repository.httpclient.FileClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserProfileRepository userProfileRepository;
  private final UserProfileMapper userProfileMapper;
  private final FileClient fileClient;

  public UserProfileResponse createUserProfile(UserProfileCreationRequest request) {
    UserProfile userProfile = userProfileMapper.toUserProfile(request);
    userProfileRepository.save(userProfile);

    return userProfileMapper.toUserProfileResponse(userProfile);
  }

  public UserProfileResponse getUserProfile(String userProfileId) {
    UserProfile userProfile = userProfileRepository.findById(userProfileId)
        .orElseThrow(() -> new RuntimeException("User Profile not found"));

    return userProfileMapper.toUserProfileResponse(userProfile);
  }

  public UserProfileResponse getUserProfileByUserId(String userId) {
    UserProfile userProfile = userProfileRepository.findByUserId(userId);

    return userProfileMapper.toUserProfileResponse(userProfile);
  }

  public List<UserProfileResponse> getAllUserProfile() {
    List<UserProfile> userProfiles = userProfileRepository.findAll();

    return userProfiles.stream().map(userProfileMapper::toUserProfileResponse).toList();

  }

  public UserProfileResponse getMyProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserProfile userProfile = userProfileRepository.findByUserId(userId);
    if (null == userProfile) {
      throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }

    return userProfileMapper.toUserProfileResponse(userProfile);
  }

  public UserProfileResponse updateMyProfile(UpdateProfileRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserProfile userProfile = userProfileRepository.findByUserId(userId);
    if (null == userProfile) {
      throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }

    userProfileMapper.updateUserProfile(userProfile, request);
    userProfileRepository.save(userProfile);

    return userProfileMapper.toUserProfileResponse(userProfile);
  }

  public UserProfileResponse updateAvatar(MultipartFile file) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserProfile userProfile = userProfileRepository.findByUserId(userId);
    if (null == userProfile) {
      throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }

    ApiResponse<FileResponse> apiResponse = fileClient.uploadAvatar(file);
    userProfile.setAvatar(apiResponse.getResult().getDownloadUrl());

    userProfileRepository.save(userProfile);
    return userProfileMapper.toUserProfileResponse(userProfile);
  }
}
