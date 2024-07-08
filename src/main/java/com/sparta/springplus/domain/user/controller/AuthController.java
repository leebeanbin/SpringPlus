package com.sparta.springplus.domain.user.controller;

import com.sparta.springplus.domain.common.ApiResponse;
import com.sparta.springplus.domain.user.dto.SignupRequestDto;
import com.sparta.springplus.domain.user.dto.UpdatePasswordReq;
import com.sparta.springplus.domain.user.dto.UserSearchResponseDto;
import com.sparta.springplus.domain.user.dto.UserUpdateRequestDto;
import com.sparta.springplus.domain.user.service.UserService;
import com.sparta.springplus.global.security.UserDetailsImpl;
import com.sun.istack.NotNull;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/profile")
    public ResponseEntity<UserSearchResponseDto> getUserProfile(
            @RequestParam String username,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok().body(userService.getUserProfile(username ,userDetails.getUser()));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestBody @Valid UserUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                userService.updateProfile(userDetails.getUser().getId(), requestDto));
    }

    @PatchMapping
    public ResponseEntity<?> updatePassword(
            @Valid @RequestBody UpdatePasswordReq updatePasswordReq,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(userDetails.getUser(), updatePasswordReq);
        return ResponseEntity.ok("Password updated");
    }
}
