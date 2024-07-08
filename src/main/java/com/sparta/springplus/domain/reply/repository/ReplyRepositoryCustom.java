package com.sparta.springplus.domain.reply.repository;


import com.sparta.springplus.domain.reply.dto.ReplyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepositoryCustom {
    Page<ReplyResponseDto> findLikedReplyByUserIdAndFeedId(@Param("userId") Long userId,@Param("feedId") Long feedId, Pageable pageable);
}
