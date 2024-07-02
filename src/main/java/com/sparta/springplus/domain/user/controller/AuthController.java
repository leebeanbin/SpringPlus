package com.sparta.springplus.domain.user.controller;

import com.sparta.springplus.domain.common.ApiResponse;
import com.sparta.springplus.domain.user.dto.SignupRequestDto;
import com.sparta.springplus.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final UserService userService;

    @PostMapping
    public ApiResponse signUp(@RequestBody SignupRequestDto request) {
        return ApiResponse.success(userService.signup(request));
    }

}
