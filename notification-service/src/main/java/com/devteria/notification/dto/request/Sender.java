package com.devteria.notification.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sender {

    private String name;
    private String email;
}
