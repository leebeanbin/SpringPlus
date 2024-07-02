package com.sparta.springplus.domain.reply.service;

import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.exception.CustomException;
import com.sparta.springplus.domain.reply.dto.ReplyRequestDto;
import com.sparta.springplus.domain.reply.dto.ReplyResponseDto;
import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.domain.reply.Reply;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.domain.feed.repository.FeedRepository;
import com.sparta.springplus.domain.reply.repository.ReplyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final FeedRepository feedRepository;

    public ReplyResponseDto createReply(long feedId, ReplyRequestDto replyRequestDto, User user) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));

        Reply reply = new Reply(replyRequestDto, feed, user);
        Reply saveReply = replyRepository.save(reply);

        return new ReplyResponseDto(saveReply);
    }

    public void deleteReply(long feedId, long replyId, User user) {
        Reply reply = replyRepository.findById(replyId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REPLY));

        if (reply.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorType.WRONG_USER_REPLY);
        }
        replyRepository.delete(reply);
    }

    @Transactional
    public ReplyResponseDto updateReply(long feedId, long replyId, ReplyRequestDto requestDto,
        User user) {
        Reply reply = replyRepository.findById(replyId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REPLY));

        if (reply.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorType.WRONG_USER_REPLY);
        }
        reply.update(requestDto);
        return new ReplyResponseDto(reply);
    }

    public List<ReplyResponseDto> findRepliesAll(long feedId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));

        return replyRepository.findAllByFeedId(feed.getId()).stream()
                .map(ReplyResponseDto::new)
                .toList();
    }

}
