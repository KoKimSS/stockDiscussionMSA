package com.example.stockmsastock.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetStockBySortRequestDto {
    private String sortBy;
    private String sortOrder;
    private int page;
    private int size;
}
