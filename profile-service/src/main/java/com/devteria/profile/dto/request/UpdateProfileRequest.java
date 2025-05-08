package com.devteria.profile.dto.request;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {
  String firstName;
  String lastName;
  String email;
  String city;
  LocalDate dob;
  String avatar;

}
