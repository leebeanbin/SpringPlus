package com.sparta.codeplanet.domain.user.repository;

import com.sparta.codeplanet.domain.user.UserPasswordLog;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserPasswordLogRepersitory extends CrudRepository<UserPasswordLog, Long> {

    List<UserPasswordLog> findTop3ByUserIdOrderByCreatedAtDesc(Long userId);
}