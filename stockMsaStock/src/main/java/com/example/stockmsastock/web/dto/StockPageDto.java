package com.example.stockmsastock.web.dto;

import com.example.stockmsastock.domain.stock.Stock;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
@NoArgsConstructor
@ToString
public class StockPageDto {
    private List<StockDto> content;
    private long totalElements;
    private int totalPages;
    private int size;
    private int numberOfElements;



    @Builder
    private StockPageDto(List<StockDto> content, long totalElements, int totalPages, int size, int numberOfElements) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.numberOfElements = numberOfElements;
    }
}
