package com.sparta.springplus.domain.likes.repository;

import com.sparta.springplus.domain.feed.dto.FeedResponseDto;
import com.sparta.springplus.domain.likes.FeedLikes;
import java.awt.print.Pageable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikesRepository extends JpaRepository<FeedLikes, Integer> {

    Optional<FeedLikes> findByFeedIdAndUserId(Long feedId, Long userId);
}
