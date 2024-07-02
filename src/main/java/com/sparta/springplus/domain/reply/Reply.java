package com.sparta.springplus.domain.reply;

import com.sparta.springplus.domain.common.TimeStamp;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.domain.reply.dto.ReplyRequestDto;
import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.domain.likes.ReplyLikes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "feedId", nullable = false)
    private Feed feed;

    @Column(nullable = false)
    @NotBlank(message = "빈 댓글은 달 수 없습니다.")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "reply")
    private List<ReplyLikes> likesList;

    @Column
    private Integer likesCount = 0;

    public int increaseLikesCount() {
        return ++likesCount;
    }

    public int decreaseLikesCount() {
        return --likesCount;
    }

    public Reply(ReplyRequestDto replyRequestDto, Feed feed, User user) {
        this.content = replyRequestDto.getContent();
        this.feed = feed;
        this.user = user;
    }

    public void update(ReplyRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
