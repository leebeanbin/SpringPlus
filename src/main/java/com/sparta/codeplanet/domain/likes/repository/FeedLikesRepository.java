package com.sparta.codeplanet.domain.likes.repository;

import com.sparta.codeplanet.domain.likes.FeedLikes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikesRepository extends JpaRepository<FeedLikes, Integer> {

    Optional<FeedLikes> findByFeedIdAndUserId(Long feedId, Long userId);

}
