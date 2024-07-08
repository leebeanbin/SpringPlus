package com.sparta.springplus.domain.follow.repository;

import static com.sparta.springplus.domain.follow.QFollow.follow;
import static com.sparta.springplus.domain.user.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springplus.domain.follow.dto.FollowResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FollowResponseDto> findUserByFollowerDesc(Pageable pageable) {
        List<FollowResponseDto> results = jpaQueryFactory
                .select(Projections.constructor(FollowResponseDto.class,
                        user.id,
                        user.company.name,
                        user.nickname,
                        user.intro,
                        user.email,
                        Expressions.asBoolean(false) // Placeholder for follow status, it will be updated later
                ))
                .from(user)
                .leftJoin(user.followerList, follow)
                .groupBy(user.id, user.company.name, user.nickname, user.intro, user.email)
                .orderBy(follow.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(user)
                .leftJoin(user.followerList, follow)
                .groupBy(user.id)
                .fetch().size();

        // Update follow status for each result
        results.forEach(followResponseDto -> {
            boolean isFollowed = !jpaQueryFactory
                    .selectFrom(follow)
                    .where(follow.fromUser.id.eq(followResponseDto.getUserId())
                            .and(follow.toUser.id.eq(followResponseDto.getUserId())))
                    .fetch().isEmpty();
            followResponseDto.setFollowStatus(isFollowed);
        });

        return new PageImpl<>(results, pageable, total);
    }
}
