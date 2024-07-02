package com.sparta.codeplanet.domain.likes;

import com.sparta.codeplanet.domain.reply.Reply;
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
@Table(name = "replyLikes")
@NoArgsConstructor
public class ReplyLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeId")
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "replyId")
    private Reply reply;

    public ReplyLikes(Reply reply, User user) {
        this.user = user;
        this.reply = reply;
    }
}
