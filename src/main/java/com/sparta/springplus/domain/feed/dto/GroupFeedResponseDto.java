package com.sparta.springplus.domain.feed.dto;

import com.sparta.springplus.domain.feed.Feed;
import lombok.Getter;

@Getter
public class GroupFeedResponseDto {
    private Long id;
    private String company;
    private String title;
    private String content;

    public GroupFeedResponseDto(Feed feed) {
        this.id = feed.getId();
        this.company = feed.getUser().getCompany().getName();
        this.title = feed.getTitle();
        this.content = feed.getContent();
    }
}
