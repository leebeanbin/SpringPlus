package com.sparta.springplus.domain.feed.dto;

import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.domain.feed.Feed;
import lombok.Getter;

@Getter
public class FeedResponseDto {
    private Long id;
    private String title;
    private String content;
    private String user;
    private Status status;

    public FeedResponseDto(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.user = feed.getUser().getUsername();
        this.status = feed.getStatus();
    }
}
