package com.example.stockmsaactivity.web.dto.request.poster;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class GetPostersByStockCodeRequest {
    String stockCode;
    private int page;
    private int size;

    @Builder
    private GetPostersByStockCodeRequest(String stockCode, int page, int size) {
        this.stockCode = stockCode;
        this.page = page;
        this.size = size;
    }
}
