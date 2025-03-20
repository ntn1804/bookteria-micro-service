package com.devteria.notification.controller;

import com.devteria.event.dto.NotificationEvent;
import com.devteria.notification.dto.request.Recipient;
import com.devteria.notification.dto.request.UserEmailRequest;
import com.devteria.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationController {

  private final EmailService emailService;

  @KafkaListener(topics = "notification-delivery")
  public void listenNotificationDelivery(NotificationEvent message) {
    log.info("Kafka message: {}", message.toString());

    UserEmailRequest userEmailRequest = UserEmailRequest.builder()
        .recipient(Recipient.builder()
            .email(message.getRecipientEmail())
            .build())
        .subject(message.getSubject())
        .htmlContent(message.getBody())
        .build();

    emailService.sendEmail(userEmailRequest);

  }

}
