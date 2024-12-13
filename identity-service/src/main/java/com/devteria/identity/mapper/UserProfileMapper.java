package com.devteria.identity.mapper;

import com.devteria.identity.dto.request.UserCreationRequest;
import com.devteria.identity.dto.request.UserProfileRequest;
import org.mapstruct.Mapper;

@Mapper
public interface UserProfileMapper {
  UserProfileRequest toUserProfileRequest(UserCreationRequest userCreationRequest);
}
