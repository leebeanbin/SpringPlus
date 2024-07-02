package com.sparta.springplus.domain.company.dto;

import com.sparta.springplus.global.enums.Status;
import com.sparta.springplus.domain.company.Company;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CompanyResponseDto {

    private Long companyId;
    private String name;
    private String domain;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CompanyResponseDto(Company company) {
        this.companyId = company.getId();
        this.name = company.getName();
        this.domain = company.getDomain();
        this.status = company.getStatus();
        this.createdAt = company.getCreatedAt();
        this.updatedAt = company.getUpdatedAt();
    }
}