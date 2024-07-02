package com.sparta.codeplanet.domain.reply.repository;

import com.sparta.codeplanet.domain.reply.Reply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByFeedId(long feedId);
}
