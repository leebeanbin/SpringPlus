package com.sparta.springplus.domain.feed.dto;

import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.global.enums.Status;
import lombok.Getter;

@Getter
public class FeedResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String user;
    private final Status status;

    public FeedResponseDto(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.user = feed.getUser().getUsername();
        this.status = feed.getStatus();
    }
}
