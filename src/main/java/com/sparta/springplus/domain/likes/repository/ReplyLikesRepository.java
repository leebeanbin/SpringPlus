package com.sparta.springplus.domain.likes.repository;

import com.sparta.springplus.domain.likes.ReplyLikes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyLikesRepository extends JpaRepository<ReplyLikes, Integer> {

    Optional<ReplyLikes> findByReplyIdAndUserId(long replyId, long userId);

}
