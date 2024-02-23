package com.example.stockmsastock.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindByItemCodeRequestDto {
    @NotBlank
    private String itemCode;

    @Builder
    private FindByItemCodeRequestDto(String itemCode) {
        this.itemCode = itemCode;
    }
}
