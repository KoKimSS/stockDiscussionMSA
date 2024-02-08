package com.example.stockmsastock.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindByNameRequestDto {
    String name;

    @Builder
    private FindByNameRequestDto(String name) {
        this.name = name;
    }
}
