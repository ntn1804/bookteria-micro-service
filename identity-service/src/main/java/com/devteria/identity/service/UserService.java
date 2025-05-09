package com.devteria.identity.service;

import com.devteria.event.dto.NotificationEvent;
import com.devteria.identity.constant.PredefinedRole;
import com.devteria.identity.dto.request.UserCreationRequest;
import com.devteria.identity.dto.request.UserProfileRequest;
import com.devteria.identity.dto.request.UserUpdateRequest;
import com.devteria.identity.dto.response.UserResponse;
import com.devteria.identity.entity.Role;
import com.devteria.identity.entity.User;
import com.devteria.identity.exception.AppException;
import com.devteria.identity.exception.ErrorCode;
import com.devteria.identity.mapper.UserMapper;
import com.devteria.identity.mapper.UserProfileMapper;
import com.devteria.identity.repository.RoleRepository;
import com.devteria.identity.repository.UserRepository;
import com.devteria.identity.repository.httpclient.ProfileClient;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

  UserRepository userRepository;
  RoleRepository roleRepository;
  ProfileClient profileClient;
  UserMapper userMapper;
  UserProfileMapper userProfileMapper;
  PasswordEncoder passwordEncoder;
  KafkaTemplate<String, Object> kafkaTemplate;

  @Transactional
  public UserResponse createUser(UserCreationRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    HashSet<Role> roles = new HashSet<>();
    roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

    user.setRoles(roles);
    user = userRepository.save(user);

    UserProfileRequest userProfileRequest = userProfileMapper.toUserProfileRequest(request);
    userProfileRequest.setUserId(user.getId());

    profileClient.createUserProfile(userProfileRequest);

    NotificationEvent notificationEvent = NotificationEvent.builder()
        .channel("EMAIL")
        .recipientEmail(user.getEmail())
        .subject("Welcome to our App")
        .body("Hello, " + user.getUsername())
        .build();

    kafkaTemplate.send("notification-delivery", notificationEvent);

    return userMapper.toUserResponse(user);
  }

  public UserResponse getMyInfo() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();

    User user = userRepository.findByUsername(name)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    return userMapper.toUserResponse(user);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse updateUser(String userId, UserUpdateRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    userMapper.updateUser(user, request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getUsers() {
    log.info("In method get Users");
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse getUser(String id) {
    return userMapper.toUserResponse(
        userRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
  }
}
