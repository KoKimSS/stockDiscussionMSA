package com.example.stockmsastock.web.dto.request;

import com.example.stockmsastock.common.Enum;
import com.example.stockmsastock.common.error.ValidationMessage;
import com.example.stockmsastock.repository.StockSortOrder;
import com.example.stockmsastock.repository.StockSortType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class GetStockPageOrderByRequestDto {
    @NotNull
    private int page;
    @NotNull
    private int size;
    @Enum(enumClass = StockSortType.class,message = ValidationMessage.NOT_STOCK_SORT_TYPE)
    private StockSortType sortBy;
    @Enum(enumClass = StockSortType.class,message = ValidationMessage.NOT_STOCK_SORT_ORDER)
    private StockSortOrder sortOrder;

    @Builder
    private GetStockPageOrderByRequestDto(int page, int size, StockSortType sortBy, StockSortOrder sortOrder) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }
}
