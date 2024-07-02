package com.sparta.springplus.domain.feed;

import com.sparta.springplus.domain.common.TimeStamp;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.domain.likes.FeedLikes;
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
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<FeedLikes> likesList;

    @Column
    private Integer likesCount = 0;

    @Builder
    public Feed(User user, String title, String content, Status status) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status != null ? status : Status.ACTIVE;

    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public int increaseLikesCount() {
        return ++likesCount;
    }

    public int decreaseLikesCount() {
        return --likesCount;
    }
}
