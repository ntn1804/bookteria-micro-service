package com.devteria.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEmailRequest {

    private Recipient recipient;
    private String subject;
    private String htmlContent;
}
