package com.example.stockmsastock.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetPriceByCodeDto {
    String itemCode;

    @Builder
    private GetPriceByCodeDto(String itemCode) {
        this.itemCode = itemCode;
    }
}
