package com.sparta.springplus.domain.user.dto;

import com.sparta.springplus.global.enums.UserRole;

public record SignInResponseDto(
        String username,
        UserRole role,
        String accessToken,
        String refreshToken
) {
}
