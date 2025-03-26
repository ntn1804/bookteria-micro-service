package com.devteria.post.dto.response;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
  String id;
  String userId;
  String firstName;
  String lastName;
  LocalDate dob;
  String city;
}
