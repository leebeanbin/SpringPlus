package com.sparta.codeplanet.domain.company.dto;

import com.sparta.codeplanet.global.enums.Status;
import com.sparta.codeplanet.domain.company.Company;
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