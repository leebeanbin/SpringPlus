package com.sparta.springplus.domain.follow.dto;

import com.sparta.springplus.domain.user.User;
import lombok.Getter;

@Getter
public class FollowResponseDto {

    private Long userId;
    private String companyName;
    private String userName;
    private String userIntro;
    private String userEmail;
    private String followStatus;

    public FollowResponseDto(User user) {
        this.userId = user.getId();
        this.companyName = user.getCompany().getName();
        this.userName = user.getNickname();
        this.userIntro = user.getIntro();
        this.userEmail = user.getEmail();
    }

    public FollowResponseDto(Long userId, String companyName, String userName, String userIntro, String userEmail, boolean followStatus){
        this.userId = userId;
        this.companyName = companyName;
        this.userName = userName;
        this.userIntro = userIntro;
        this.userEmail = userEmail;
        setFollowStatus(followStatus);
    }

    /**
     * 팔로우 여부
     */
    public void setFollowStatus(boolean followStatus) {
        this.followStatus = followStatus ? "Followed" : "Unfollowed";
    }
}
