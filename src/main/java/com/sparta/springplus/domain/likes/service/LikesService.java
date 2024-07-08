package com.sparta.springplus.domain.likes.service;

import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.domain.user.repository.UserRepository;
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
import com.sparta.springplus.global.security.UserDetailsServiceImpl;
import java.util.Optional;
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
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public Long likeFeed(long feedId, UserDetailsImpl userDetails) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));

        if (feed.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorType.SAME_USER_FEED);
        }

        Optional<FeedLikes> feedLikes = feedLikesRepository.findByFeedIdAndUserId(feedId,
                userDetails.getUser().getId());
        // 중복 제거
        boolean isLike = feedLikes.isPresent();
        if (isLike) {
            throw new CustomException(ErrorType.DUPLICATE_LIKE);
        }

        // 새로 생긴 좋아요를 list에 추가
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(ErrorType.NOT_EXISTS_USER));
        // add feed I marked as i like
        user.addLikeFeed(feed);
        FeedLikes newFeedLikes = new FeedLikes(feed, user);

        // 좋아하는 게시물 Feed List에 추가
        feedLikesRepository.save(newFeedLikes);
        feed.addFeedLikes(newFeedLikes);

        return feed.increaseLikesCount();
    }

    @Transactional
    public Long unlikeFeed(long feedId, UserDetailsImpl userDetails) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));

        FeedLikes feedLikes = feedLikesRepository
                .findByFeedIdAndUserId(feedId, userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_LIKE));

        feedLikesRepository.delete(feedLikes);
        userDetails.getUser().deleteFeedLike(feed);
        feed.deleteFeedLike(feedLikes);

        return feed.decreaseLikesCount();
    }

    @Transactional
    public int likeReply(long replyId, UserDetailsImpl userDetails) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REPLY));

        if (reply.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorType.SAME_USER_REPLY);
        }

        if (replyLikesRepository.findByReplyIdAndUserId(replyId, userDetails.getUser().getId())
                .isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_LIKE);
        }

        ReplyLikes replyLikes = new ReplyLikes(reply, userDetails.getUser());
        replyLikesRepository.save(replyLikes);
        reply.addReplyLikes(replyLikes);
        reply.getUser().addLikeReply(reply);
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
        reply.deleteReplyLikes(replyLikes);
        reply.getUser().deleteReplyLike(reply);

        return reply.decreaseLikesCount();
    }


}
