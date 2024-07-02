package com.sparta.codeplanet.domain.reply.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyRequestDto {

    @NotBlank
    private String content;

    public ReplyRequestDto(String content) {
        this.content = content;
    }
}
