package com.sparta.codeplanet.domain.feed.dto;

import com.sparta.codeplanet.global.enums.Status;
import com.sparta.codeplanet.domain.feed.Feed;
import com.sparta.codeplanet.domain.user.User;
import lombok.Getter;

@Getter
public class FeedRequestDto {
    private String title;
    private String content;
    private Status status;

    public Feed toEntity(User user) {
        return Feed.builder()
            .user(user)
            .title(title)
            .content(content)
            .status(status != null ? status : Status.ACTIVE)
            .build();

    }
}
