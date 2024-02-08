package com.example.stockmsastock.domain.stock;

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
    private String stockName; //종목 코드
    private String category; // 시장 구분 (KOSPI/KOSDAQ/KONEX 등)

    @Builder
    public Stock(Long id, String itemCode, String stockName, String category) {
        this.id = id;
        this.itemCode = itemCode;
        this.stockName = stockName;
        this.category = category;
    }
}
