package com.devteria.profile.dto.request;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileCreationRequest {

  String firstName;
  String lastName;
  LocalDate dob;
  String city;
}
