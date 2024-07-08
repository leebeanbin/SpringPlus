package com.sparta.springplus.domain.feed.repository;

import com.sparta.springplus.domain.feed.dto.GetEachFeedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepositoryCustom {
    Page<GetEachFeedDto> findLikedFeedsByUserId(@Param("userId") Long userId, Pageable pageable);
}
