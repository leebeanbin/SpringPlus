package com.sparta.springplus.domain.likes.service;

import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.exception.CustomException;
import com.sparta.springplus.global.security.UserDetailsImpl;
import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.domain.reply.Reply;
import com.sparta.springplus.domain.likes.FeedLikes;
import com.sparta.springplus.domain.likes.ReplyLikes;
import com.sparta.springplus.domain.feed.repository.FeedRepository;
import com.sparta.springplus.domain.reply.repository.ReplyRepository;
import com.sparta.springplus.domain.likes.repository.FeedLikesRepository;
import com.sparta.springplus.domain.likes.repository.ReplyLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final FeedLikesRepository feedLikesRepository;
    private final ReplyLikesRepository replyLikesRepository;
    private final FeedRepository feedRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public int likeFeed(long feedId, UserDetailsImpl userDetails) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));

        if (feed.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorType.SAME_USER_FEED);
        }

        if (feedLikesRepository.findByFeedIdAndUserId(feedId, userDetails.getUser().getId()).isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_LIKE);
        }

        feedLikesRepository.save(new FeedLikes(feed,userDetails.getUser()));

        return feed.increaseLikesCount();
    }

    @Transactional
    public int unlikeFeed(long feedId, UserDetailsImpl userDetails) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));

        FeedLikes feedLikes = feedLikesRepository
                .findByFeedIdAndUserId(feedId, userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_LIKE));

        feedLikesRepository.delete(feedLikes);

        return feed.decreaseLikesCount();
    }

    @Transactional
    public int likeReply(long replyId, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(replyId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REPLY));

        if (reply.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorType.SAME_USER_REPLY);
        }

        if (replyLikesRepository.findByReplyIdAndUserId(replyId, userDetails.getUser().getId()).isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_LIKE);
        }

        replyLikesRepository.save(new ReplyLikes(reply,userDetails.getUser()));

        return reply.increaseLikesCount();
    }

    @Transactional
    public int unlikeReply(long replyId, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(replyId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REPLY));

        ReplyLikes replyLikes = replyLikesRepository
                .findByReplyIdAndUserId(replyId, userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_LIKE));

        replyLikesRepository.delete(replyLikes);

        return reply.decreaseLikesCount();
    }



}
