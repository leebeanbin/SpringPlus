package com.sparta.springplus.domain.user.dto;

import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.global.enums.UserRole;
import lombok.Getter;

/**
 * this dto is going to be used only for saving data and delivery them to Entity.
 */
@Getter
public class UserSearchResponseDto {

    private final String username;
    private final String nickname;
    private final String intro;
    private final String email;
    private final UserRole userRole;
    private final Status status;
    private final Long feedLikeAmount;
    private final Long replyLikeAmount;

    public UserSearchResponseDto(User user){
        username = user.getUsername();
        nickname = user.getNickname();
        intro = user.getIntro();
        email = user.getEmail();
        userRole = user.getUserRole();
        status = user.getStatus();
        feedLikeAmount = user.getFeedLikeAmount();
        replyLikeAmount = user.getReplyLikeAmount();
    }

}
