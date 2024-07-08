package com.sparta.springplus.domain.reply.repository;

import com.sparta.springplus.domain.reply.Reply;
import com.sparta.springplus.domain.reply.dto.ReplyResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByFeedId(long feedId);

    @Query(
            "SELECT new com.sparta.springplus.domain.reply.dto.ReplyResponseDto(r)" +
            "FROM Reply r JOIN r.likesList l " +
                    "where l.user.id = :userId AND l.reply.feed.id = :feedId " +
                    "ORDER BY r.createdAt DESC"
    )
    Page<ReplyResponseDto> findLikedReplyByUserIdAndFeedId(@Param("userId") Long userId,@Param("feedId") Long feedId, Pageable pageable);
}
