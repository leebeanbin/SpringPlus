package com.sparta.codeplanet.domain.likes.repository;

import com.sparta.codeplanet.domain.likes.ReplyLikes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyLikesRepository extends JpaRepository<ReplyLikes, Integer> {

    Optional<ReplyLikes> findByReplyIdAndUserId(long replyId, long userId);

}
