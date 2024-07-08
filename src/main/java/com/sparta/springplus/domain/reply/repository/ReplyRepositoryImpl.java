package com.sparta.springplus.domain.reply.repository;

import static com.sparta.springplus.domain.likes.QReplyLikes.replyLikes;
import static com.sparta.springplus.domain.reply.QReply.reply;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springplus.domain.reply.dto.ReplyResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ReplyResponseDto> findLikedReplyByUserIdAndFeedId(Long userId, Long feedId,
            Pageable pageable) {
        BooleanExpression predicate = replyLikes.user.id.eq(userId)
                .and(replyLikes.reply.feed.id.eq(feedId));

        List<ReplyResponseDto> results = jpaQueryFactory
                .select(Projections.constructor(ReplyResponseDto.class,
                        reply.id,
                        reply.content,
                        reply.feed.id,
                        reply.user.id,
                        reply.createdAt,
                        reply.updatedAt,
                        reply.likesCount))
                .from(reply)
                .join(reply.likesList, replyLikes)
                .where(predicate)
                .orderBy(reply.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(reply)
                .join(reply.likesList, replyLikes)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
