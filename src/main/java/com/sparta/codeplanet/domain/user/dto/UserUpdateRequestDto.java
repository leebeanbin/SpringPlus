package com.sparta.codeplanet.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private String intro;
    @NotBlank
    private String nickname;

    // ture 들어오면 탈퇴
    private Boolean isTalTye;
}