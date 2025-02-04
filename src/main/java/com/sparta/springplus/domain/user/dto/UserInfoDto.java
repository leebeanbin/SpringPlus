package com.sparta.springplus.domain.user.dto;

import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.global.enums.UserRole;
import com.sparta.springplus.domain.user.User;
import lombok.Getter;

@Getter
public class UserInfoDto {

    private Long userId;
    private String companyName;
    private String email;
    private String nickname;
    private String intro;
    private UserRole role;
    private Status status;

    public UserInfoDto(User user) {
        this.userId = user.getId();
        this.companyName = user.getCompany().getName();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.intro = user.getIntro();
        this.role = user.getUserRole();
        this.status = user.getStatus();
    }
}