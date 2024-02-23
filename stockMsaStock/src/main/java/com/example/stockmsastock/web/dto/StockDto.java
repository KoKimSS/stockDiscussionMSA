package com.example.stockmsastock.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockDto {
    private Long id;
    private String itemCode; // 종목 코드
    private String stockName; // 종목 이름
    private String category; // 시장 구분 (KOSPI/KOSDAQ/KONEX 등)
    private Long accumulatedTradingVolume; // 거래량
    private Long accumulatedTradingValue; // 거래대금
    private Double fluctuationsRatio; // 상승률

    @Builder
    private StockDto(Long id, String itemCode, String stockName, String category, Long accumulatedTradingVolume, Long accumulatedTradingValue, Double fluctuationsRatio) {
        this.id = id;
        this.itemCode = itemCode;
        this.stockName = stockName;
        this.category = category;
        this.accumulatedTradingVolume = accumulatedTradingVolume;
        this.accumulatedTradingValue = accumulatedTradingValue;
        this.fluctuationsRatio = fluctuationsRatio;
    }
}