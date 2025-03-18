package com.devteria.notification.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    private Sender sender;
    private List<Recipient> to;
    private String subject;
    private String htmlContent;
}
