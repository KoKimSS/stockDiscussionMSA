package com.example.stockmsastock.domain.stockPrice;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class StockPrice {
    private String itemCode; //종목 코드
    private String stockName; //종목 이름
    private String closePrice;
    private String compareToPreviousClosePrice;
    private String fluctuationsRatio;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String accumulatedTradingVolume;
    private String accumulatedTradingValue;

    @Builder
    private StockPrice(String itemCode, String stockName, String closePrice, String compareToPreviousClosePrice, String fluctuationsRatio, String openPrice, String highPrice, String lowPrice, String accumulatedTradingVolume, String accumulatedTradingValue) {
        this.itemCode = itemCode;
        this.stockName = stockName;
        this.closePrice = closePrice;
        this.compareToPreviousClosePrice = compareToPreviousClosePrice;
        this.fluctuationsRatio = fluctuationsRatio;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.accumulatedTradingVolume = accumulatedTradingVolume;
        this.accumulatedTradingValue = accumulatedTradingValue;
    }
}
