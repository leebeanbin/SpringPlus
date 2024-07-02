package com.sparta.codeplanet.domain.user.dto;

import com.sparta.codeplanet.global.enums.UserRole;

public record SignInResponseDto(
        String username,
        UserRole role,
        String accessToken,
        String refreshToken
) {
}
