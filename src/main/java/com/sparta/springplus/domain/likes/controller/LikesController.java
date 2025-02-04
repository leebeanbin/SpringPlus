package com.sparta.springplus.domain.likes.controller;

import com.sparta.springplus.global.enums.ResponseMessage;
import com.sparta.springplus.global.security.UserDetailsImpl;
import com.sparta.springplus.domain.user.dto.ResponseEntityDto;
import com.sparta.springplus.domain.likes.service.LikesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed/{feedId}")
public class LikesController {
    private  final LikesService likesService;

    public LikesController(final LikesService likesService) {
        this.likesService = likesService;
    }

    @PostMapping("/likes")
    public ResponseEntity<?> addLikesToFeed(@PathVariable("feedId") long feedId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
//            HttpResponseDto.builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("이 피드 좋아요!")
//                .data(likesService.likeFeed(feedId, userDetails))
//                .build()
                new ResponseEntityDto<>(
                        ResponseMessage.ADD_LIKE_TO_FEED_SUCCESS,
                        likesService.likeFeed(feedId, userDetails)
                )
        );
    }

    @DeleteMapping("/likes")
    public ResponseEntity<?> subLikesToFeed(@PathVariable("feedId") long feedId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
//            HttpResponseDto.builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("피드 좋아요 취소!")
//                .data(likesService.unlikeFeed(feedId, userDetails))
//                .build()
                new ResponseEntityDto<>(
                        ResponseMessage.CANCEL_LIKE_TO_FEED_SUCCESS,
                        likesService.unlikeFeed(feedId, userDetails)
                )
        );
    }

    @PostMapping("/reply/{replyId}/likes")
    public ResponseEntity<?> addLikesToReply(@PathVariable("feedId") long feedId,
        @PathVariable("replyId") int replyId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
//            HttpResponseDto.builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("이 댓글 좋아요!")
//                .data(likesService.likeReply(replyId, userDetails))
//                .build()
                new ResponseEntityDto<>(
                        ResponseMessage.ADD_LIKE_TO_REPLY_SUCCESS,
                        likesService.likeReply(replyId, userDetails)
                )
        );
    }

    @DeleteMapping("/reply/{replyId}/likes")
    public ResponseEntity<?> subLikesToReply(@PathVariable("feedId") long feedId,
        @PathVariable("replyId") int replyId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
//            HttpResponseDto.builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("댓글 좋아요 취소!")
//                .data(likesService.unlikeReply(replyId, userDetails))
//                .build()
                new ResponseEntityDto<>(
                        ResponseMessage.CANCEL_LIKE_TO_REPLY_SUCCESS,
                        likesService.unlikeReply(replyId, userDetails)
                )
        );
    }


}
