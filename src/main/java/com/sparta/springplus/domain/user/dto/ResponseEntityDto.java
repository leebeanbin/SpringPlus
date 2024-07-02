package com.sparta.springplus.domain.user.dto;

import com.sparta.springplus.global.enums.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseEntityDto<T> {

    private String status;
    private String message;
    private T data;

    public ResponseEntityDto(ResponseMessage message, T data) {
        this.status = HttpStatus.OK.toString();
        this.message = message.getMessage();
        this.data = data;
    }

    public ResponseEntityDto(ResponseMessage message) {
        this.status = HttpStatus.OK.toString();
        this.message = message.getMessage();
    }
}
