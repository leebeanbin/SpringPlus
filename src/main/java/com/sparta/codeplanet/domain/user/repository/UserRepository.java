package com.sparta.codeplanet.domain.user.repository;

import com.sparta.codeplanet.global.enums.Status;
import com.sparta.codeplanet.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByUsernameAndStatus(String username, Status active);
    Optional<User> findByEmail(String email);
}
