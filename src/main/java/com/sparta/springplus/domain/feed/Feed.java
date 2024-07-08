package com.sparta.springplus.domain.feed;

import com.sparta.springplus.domain.common.TimeStamp;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.domain.likes.FeedLikes;
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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends TimeStamp {

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */

    @Builder
    public Feed(User user, String title, String content, Status status) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status != null ? status : Status.ACTIVE;
    }


    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;


    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<FeedLikes> likesList = new ArrayList<>();

    @Column
    private Long likesCount = 0L;


    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */
    public void addFeedLikes(FeedLikes feedLikes){
        likesList.add(feedLikes);
    }

    public void deleteFeedLike(FeedLikes feedLikes){
        likesList.remove(feedLikes);
    }


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long increaseLikesCount() {
        return ++likesCount;
    }

    public Long decreaseLikesCount() {
        return --likesCount;
    }
}
