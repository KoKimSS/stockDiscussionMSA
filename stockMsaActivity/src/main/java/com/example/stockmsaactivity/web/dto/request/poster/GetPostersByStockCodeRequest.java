package com.example.stockmsaactivity.web.dto.request.poster;

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
}
