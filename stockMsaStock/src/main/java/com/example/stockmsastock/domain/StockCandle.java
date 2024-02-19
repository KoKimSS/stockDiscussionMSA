package com.example.stockmsastock.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockCandle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10)
    private String code;
    private LocalDate date;
    private int open;
    private int low;
    private int high;
    private int close;
    private int volume;
    @Column(precision=8, scale=2)
    private Double bollingerBands;
    @Column(precision=8, scale=2)
    private Double macd;
    @Column(precision=8, scale=2)
    private Double movingAverage_12;
    @Column(precision=8, scale=2)
    private Double movingAverage_20;
    @Column(precision=8, scale=2)
    private Double movingAverage_26;

    @Builder
    private StockCandle(Long id, String code, LocalDate date, int open, int low, int high, int close, int volume) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.open = open;
        this.low = low;
        this.high = high;
        this.close = close;
        this.volume = volume;
    }
}
