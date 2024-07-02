package com.sparta.codeplanet.domain.email.repository;

import com.sparta.codeplanet.domain.email.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByEmail(String email);
}
