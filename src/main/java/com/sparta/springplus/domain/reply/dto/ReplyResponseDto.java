package com.sparta.springplus.domain.reply.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.springplus.domain.reply.QReply;
import com.sparta.springplus.domain.reply.Reply;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyResponseDto {

    private long id;
    private String content;
    private long feedId;
    private long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCount;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();
        this.feedId = reply.getFeed().getId();
        this.userId = reply.getUser().getId();
        this.likeCount = reply.getLikesCount();
    }

    public ReplyResponseDto(long id, String content, long feedId, long userId, LocalDateTime createdAt, LocalDateTime updatedAt, int likeCount) {
        this.id = id;
        this.content = content;
        this.feedId = feedId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
    }
}
