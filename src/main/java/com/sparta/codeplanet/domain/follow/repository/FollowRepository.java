package com.sparta.codeplanet.domain.follow.repository;

import com.sparta.codeplanet.domain.follow.Follow;
import com.sparta.codeplanet.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);
}
