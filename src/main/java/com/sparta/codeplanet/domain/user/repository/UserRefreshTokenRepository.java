package com.sparta.codeplanet.domain.user.repository;

import com.sparta.codeplanet.domain.user.User;
import com.sparta.codeplanet.domain.user.UserRefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    Optional<UserRefreshToken> findByIdAndReissueCountLessThan(Long id, long count);

    Optional<UserRefreshToken> findByRefreshToken(String refreshToken);

    UserRefreshToken findByUser(User user);
}