package com.sparta.springplus.domain.reply.dto;

import com.sparta.springplus.domain.reply.Reply;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ReplyRequestDto {

    @NotBlank
    private String content;


    public Reply toEntity(){
        return Reply.builder()
                .content(content)
                .build();
    }
}
