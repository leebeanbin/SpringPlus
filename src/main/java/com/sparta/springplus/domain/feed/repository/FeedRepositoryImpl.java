package com.sparta.springplus.domain.feed.repository;

import static com.sparta.springplus.domain.feed.QFeed.feed;
import static com.sparta.springplus.domain.likes.QFeedLikes.feedLikes;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springplus.domain.feed.dto.GetEachFeedDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<GetEachFeedDto> findLikedFeedsByUserId(Long userId, Pageable pageable) {
        BooleanExpression predicate = feedLikes.user.id.eq(userId)
                .and(feedLikes.feed.id.eq(feedLikes.id));

        List<GetEachFeedDto> results = jpaQueryFactory.
                select(Projections.constructor(GetEachFeedDto.class,
                        feed.id,
                        feed.title,
                        feed.content,
                        feed.user.username,
                        feed.status,
                        feed.likesCount))
                .from(feed)
                .join(feed.likesList, feedLikes)
                .where(predicate)
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(feed)
                .join(feed.likesList, feedLikes)
                .where(predicate)
                .fetch().size();

        return new PageImpl<>(results, pageable, total);
    }
}
