package com.sparta.springplus.domain.user;


import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.domain.likes.FeedLikes;
import com.sparta.springplus.domain.reply.Reply;
import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.global.enums.UserRole;
import com.sparta.springplus.global.exception.CustomException;
import com.sparta.springplus.domain.company.Company;
import com.sparta.springplus.domain.follow.Follow;
import com.sparta.springplus.domain.common.TimeStamp;
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
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends TimeStamp {

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String intro;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private Boolean refresh;

    @Column
    private Long feedLikeAmount = 0L;
    @Column
    private Long replyLikeAmount = 0L;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */

    @Builder
    public User(String username,String nickname, String hashedPassword, String email,Company company, String intro, Status status) {
        this.username = username;
        this.password = hashedPassword;
        this.nickname = nickname;
        this.email = email;
        this.company = company;
        this.intro = intro;
        this.status = status;
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Feed> feed = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reply> replies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY)
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    private List<Follow> followerList = new ArrayList<>();

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */
    public void addLikeFeed(Feed feed){
        this.feed.add(feed);
        ++this.feedLikeAmount;
    }

    public void deleteFeedLike(Feed feed){
        this.feed.remove(feed);
        --this.feedLikeAmount;
    }

    public void addLikeReply(Reply reply){
        this.replies.add(reply);
        ++this.replyLikeAmount;
    }

    public void deleteReplyLike(Reply reply){
        this.replies.remove(reply);
        --this.replyLikeAmount;
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */

    // status를 수정하는 매서드 만들기
    public void setStatus(String statusString) {
        if (statusString.equals("탈퇴")) {
            status = Status.DEACTIVATE;
        }
    }

    public boolean setRefresh(Boolean refresh) {
        this.refresh = refresh;;
        return this.refresh;
    }

    /**
     * 회원 상태 검증 (이메일 인증)
     */
    public void verifyStatusWhenEmailAuth() {
        // 이미 승인된 회원
        if (Status.ACTIVE.equals(this.status)) {
            throw new CustomException(ErrorType.APPROVED_USER);
        }
        // 탈퇴한 회원
        if (Status.DEACTIVATE.equals(this.status)) {
            throw new CustomException(ErrorType.DEACTIVATED_USER);
        }
    }

    /**
     * 회원 상태 검증 (팔로우)
     */
    public void verifyStatusWhenFollow() {
        // 승인되지 않은 회원
        if (Status.BEFORE_APPROVE.equals(this.status)) {
            throw new CustomException(ErrorType.UNAPPROVED_USER);
        }
        // 탈퇴한 회원
        if (Status.DEACTIVATE.equals(this.status)) {
            throw new CustomException(ErrorType.DEACTIVATED_USER);
        }
    }

    /**
     * 회원 상태 변경 (ACTIVE)
     */
    public void active() {
        this.status = Status.ACTIVE;
    }

    /**
     * 회원 상태 변경 (DEACTIVATE)
     */
    public void deactivate() {
        this.status = Status.DEACTIVATE;
    }

    public void updateRole() {
        if (this.userRole == UserRole.USER) {
            this.userRole = UserRole.ADMIN;
        }else {
            this.userRole = UserRole.USER;
        }
    }

    public void updatePassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public void updateProfile(String nickname, String intro) {
        this.nickname = nickname;
        this.intro = intro;
    }
}