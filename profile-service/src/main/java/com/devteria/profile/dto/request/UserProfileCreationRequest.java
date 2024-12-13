package com.devteria.profile.dto.request;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileCreationRequest {

  private String userId;
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private String city;
}
