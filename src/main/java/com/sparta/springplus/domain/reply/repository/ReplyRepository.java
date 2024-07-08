package com.sparta.springplus.domain.reply.repository;

import com.sparta.springplus.domain.reply.Reply;
import com.sparta.springplus.domain.reply.dto.ReplyResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {

    List<Reply> findAllByFeedId(long feedId);

}
