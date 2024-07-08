package com.sparta.springplus.domain.feed.dto;

import static com.sparta.springplus.domain.feed.QFeed.feed;

import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.global.enums.Status;
import lombok.Getter;


@Getter
public class GetEachFeedDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String username;
    private final Status status;
    private final Long likes;

    public GetEachFeedDto(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.username = feed.getUser().getUsername();
        this.status = feed.getStatus();
        this.likes = feed.getLikesCount();
    }

    public GetEachFeedDto(Long id, String title, String content, String username, Status status, Long likes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.status = status;
        this.likes = likes;
    }
}
