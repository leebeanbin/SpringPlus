package com.sparta.codeplanet.domain.likes;

import com.sparta.codeplanet.domain.feed.Feed;
import com.sparta.codeplanet.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "feedLikes")
@NoArgsConstructor
public class FeedLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeId")
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "feedId")
    private Feed feed;

    public FeedLikes(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }
}
