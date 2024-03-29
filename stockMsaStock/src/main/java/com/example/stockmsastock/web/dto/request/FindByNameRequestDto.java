package com.example.stockmsastock.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindByNameRequestDto {
    @NotBlank
    String name;

    @Builder
    private FindByNameRequestDto(String name) {
        this.name = name;
    }
}
