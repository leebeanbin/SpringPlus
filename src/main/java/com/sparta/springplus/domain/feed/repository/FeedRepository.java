package com.sparta.springplus.domain.feed.repository;

import com.sparta.springplus.domain.company.Company;
import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.domain.feed.dto.GetEachFeedDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed,Long>, FeedRepositoryCustom{
    Page<Feed> findByUser_Company(Company company, Pageable pageable);
    Page<Feed> findAllByUserIdIn(List<Long> followList, Pageable pageable);
}
