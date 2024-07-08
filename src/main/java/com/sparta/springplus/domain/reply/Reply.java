package com.sparta.springplus.domain.reply;

import com.sparta.springplus.domain.common.TimeStamp;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.domain.reply.dto.ReplyRequestDto;
import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.domain.likes.ReplyLikes;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends TimeStamp {

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;


    @Column(nullable = false)
    @NotBlank(message = "빈 댓글은 달 수 없습니다.")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column
    private Integer likesCount = 0;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */

    @Builder
    public Reply(String content) {
        this.content = content;
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "feedId", nullable = false)
    private Feed feed;

    @OneToMany(mappedBy = "reply", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplyLikes> likesList = new ArrayList<>();

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */
    public void addReplyLikes(ReplyLikes replyLikes){
        likesList.add(replyLikes);
    }

    public void deleteReplyLikes(ReplyLikes replyLikes){
        likesList.remove(replyLikes);
    }


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     *
     * @return
     */
    public Reply setUserAndFeed(User user, Feed feed){
        this.user = user;
        this.feed = feed;
        return this;
    }



    public int increaseLikesCount() {
        return ++likesCount;
    }

    public int decreaseLikesCount() {
        return --likesCount;
    }


    public void update(ReplyRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
