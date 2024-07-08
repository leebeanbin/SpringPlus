package com.sparta.springplus.domain.follow.repository;

import com.sparta.springplus.domain.follow.dto.FollowResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepositoryCustom {
    Page<FollowResponseDto> findUserByFollowerDesc(Pageable pageable);
}
