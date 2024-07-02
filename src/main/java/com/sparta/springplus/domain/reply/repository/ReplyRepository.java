package com.sparta.springplus.domain.reply.repository;

import com.sparta.springplus.domain.reply.Reply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByFeedId(long feedId);
}
