package com.sparta.springplus.domain.reply.controller;

import com.sparta.springplus.domain.reply.dto.ReplyRequestDto;
import com.sparta.springplus.domain.reply.dto.ReplyResponseDto;
import com.sparta.springplus.domain.reply.service.ReplyService;
import com.sparta.springplus.domain.user.dto.ResponseEntityDto;
import com.sparta.springplus.global.enums.ResponseMessage;
import com.sparta.springplus.global.security.UserDetailsImpl;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed/{feedId}")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    /**
     * 댓글 작성
     *
     * @param feedId
     * @param replyRequestDto
     * @param userDetails
     * @return 상태 코드, 메시지, 댓글 작성 정보
     */
    @PostMapping("/reply")
    public ResponseEntity<?> createReply(@PathVariable("feedId") Long feedId,
            @RequestBody ReplyRequestDto replyRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                new ResponseEntityDto<>(
                        ResponseMessage.ADD_REPLY_SUCCESS,
                        replyService.createReply(feedId, replyRequestDto, userDetails.getUser())
                )
        );
    }

    /**
     * 댓글 조회
     *
     * @param feedId
     * @return 상태 코드, 메시지, 댓글 정보
     */
    @GetMapping("/reply")
    public ResponseEntity<?> getReplies(@PathVariable("feedId") Long feedId) {
        List<ReplyResponseDto> replies = replyService.findRepliesAll(feedId);
        if (replies.isEmpty()) {
            return ResponseEntity.ok(
                    new ResponseEntityDto<>(
                            ResponseMessage.NO_EXIST_REPLY
                    )
            );
        } else {
            return ResponseEntity.ok(
                    new ResponseEntityDto<>(
                            ResponseMessage.REPLY_READ_SUCCESS,
                            replies
                    )
            );
        }
    }

    @GetMapping("/reply/liked")
    public ResponseEntity<List<ReplyResponseDto>> getLikedRepliesWithPage(
            @PathVariable("feedId") Long feedId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok()
                .body(replyService.getLikedRepliesWithPage(userDetails.getUser().getId(), feedId,
                        page, size).getContent());
    }


    /**
     * 댓글 삭제
     *
     * @param feedId
     * @param replyId
     * @param userDetails
     * @return 상태코드, 메시지
     */
    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable("feedId") Long feedId,
            @PathVariable("replyId") Long replyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        replyService.deleteReply(feedId, replyId, userDetails.getUser());
        return ResponseEntity.ok(
                new ResponseEntityDto<>(
                        ResponseMessage.REPLY_DELETE_SUCCESS
                )
        );
    }

    /**
     * 댓글 수정
     *
     * @param feedId
     * @param replyId
     * @param requestDto
     * @param userDetails
     * @return 상태코드, 메시지, 수정된 댓글 정보
     */
    @PatchMapping("/reply/{replyId}")
    public ResponseEntity<?> updateReply(@PathVariable("feedId") Long feedId,
            @PathVariable("replyId") Long replyId,
            @RequestBody ReplyRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                new ResponseEntityDto<>(
                        ResponseMessage.REPLY_UPDATE_SUCCESS,
                        replyService.updateReply(feedId, replyId, requestDto, userDetails.getUser())
                )
        );
    }
}
