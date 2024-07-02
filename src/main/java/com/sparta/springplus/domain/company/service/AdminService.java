package com.sparta.springplus.domain.company.service;

import com.sparta.springplus.domain.user.service.UserService;
import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.global.exception.CustomException;
import com.sparta.springplus.domain.company.dto.CompanyRequestDto;
import com.sparta.springplus.domain.company.dto.CompanyResponseDto;
import com.sparta.springplus.domain.user.dto.UserInfoDto;
import com.sparta.springplus.domain.company.Company;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.domain.company.repository.CompanyRepository;
import com.sparta.springplus.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public CompanyResponseDto createCompany(CompanyRequestDto requestDto, User user) {

        Company company = Company.builder()
                .name(requestDto.getName())
                .domain(requestDto.getDomain())
                .status(Status.ACTIVE)
                .build();

        Company saveCompany = companyRepository.save(company);

        return new CompanyResponseDto(saveCompany);
    }

    public List<UserInfoDto> getAllUsers() {
        List<UserInfoDto> users = userRepository.findAll().stream().map(UserInfoDto::new).toList();

        if (users.isEmpty()) {
            throw new CustomException(ErrorType.NOT_EXIST_ALL_USERS);
        }

        return users;
    }

    @Transactional
    public UserInfoDto updateRole(Long userId) {
        User user = userService.getUserById(userId);
        user.updateRole();

        return new UserInfoDto(user);
    }
}