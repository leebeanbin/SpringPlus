package com.sparta.springplus.domain.company.service;

import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.exception.CustomException;
import com.sparta.springplus.domain.company.Company;
import com.sparta.springplus.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * 등록된 도메인인지 확인
     * @param email 이메일
     */
    public Company verifyDomainOfEmail(String email) {
        String domain = email.substring(email.indexOf("@") + 1);

        return companyRepository.findByDomain(domain).orElseThrow(
                ()-> new CustomException(ErrorType.UNREGISTERED_DOMAIN));
    }
}
