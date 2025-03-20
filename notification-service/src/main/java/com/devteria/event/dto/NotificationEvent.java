package com.devteria.event.dto;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEvent {
  String channel; // which channel will get the notification. Ex: email, zalo, slack,...
  String recipientEmail;
  String templateCode;
  Map<String, Object> param;
  String subject;
  String body;

}
