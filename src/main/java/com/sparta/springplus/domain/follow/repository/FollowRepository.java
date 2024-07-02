package com.sparta.springplus.domain.follow.repository;

import com.sparta.springplus.domain.follow.Follow;
import com.sparta.springplus.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);
}
