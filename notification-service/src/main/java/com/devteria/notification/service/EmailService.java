package com.devteria.notification.service;

import com.devteria.notification.dto.request.EmailRequest;
import com.devteria.notification.dto.request.Sender;
import com.devteria.notification.dto.request.UserEmailRequest;
import com.devteria.notification.dto.response.EmailResponse;
import com.devteria.notification.exception.AppException;
import com.devteria.notification.exception.ErrorCode;
import com.devteria.notification.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailClient emailClient;

    @Value("${client.brevo.api-key}")
    private String apiKey;

    public EmailResponse sendEmail(UserEmailRequest userEmailRequest) {

        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .email("ntn1804.dev@gmail.com")
                        .name("ntn1804.dev")
                        .build())
                .subject(userEmailRequest.getSubject())
                .htmlContent(userEmailRequest.getHtmlContent())
                .to(List.of(userEmailRequest.getRecipient()))
                .build();

        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }

    }
}
