package com.example.stockmsastock.web.dto.request;

import lombok.Data;

@Data
public class GetStockPageOrderByRequestDto {

    private int page;
    private int size;
    private String sortBy;
    private String sortOrder;
}
