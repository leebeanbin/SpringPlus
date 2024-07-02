package com.sparta.springplus.domain.email.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class EmailResponseDto {

    private String email;
    private String authCode;
    private LocalDateTime expiredAt;

    public EmailResponseDto(String email, String authCode, LocalDateTime expiredAt) {
        this.email = email;
        this.authCode = authCode;
        this.expiredAt = expiredAt;
    }
}
