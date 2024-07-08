package com.sparta.springplus.domain.feed.dto;

import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.global.security.UserDetailsImpl;
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
