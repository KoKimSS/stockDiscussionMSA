package com.example.stockbatch.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemCode; //종목 코드
    private String stockName; //종목 이름
    private String category; // 시장 구분 (KOSPI/KOSDAQ/KONEX 등)

    private String accumulatedTradingVolume; //거래량
    private String accumulatedTradingValue; //거래대금
    private String fluctuationsRatio; //상승률

    @Builder
    private Stock(Long id, String itemCode, String stockName, String category, String accumulatedTradingVolume, String accumulatedTradingValue, String fluctuationsRatio) {
        this.id = id;
        this.itemCode = itemCode;
        this.stockName = stockName;
        this.category = category;
        this.accumulatedTradingVolume = accumulatedTradingVolume;
        this.accumulatedTradingValue = accumulatedTradingValue;
        this.fluctuationsRatio = fluctuationsRatio;
    }
}
