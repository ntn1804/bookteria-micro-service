package com.devteria.identity.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequest {

  private String userId;
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private String city;
}
