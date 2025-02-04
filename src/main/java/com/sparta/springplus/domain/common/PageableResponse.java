package com.sparta.springplus.domain.common;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageableResponse<T> {
    private List<T> content;

    public PageableResponse(Page<T> page) {
        this.content = page.getContent();
    }
}
