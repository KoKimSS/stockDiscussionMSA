package com.example.stockmsastock.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup {

    private final StockCandleService stockCandleService;

    public ApplicationStartup(StockCandleService stockCandleService) {
        this.stockCandleService = stockCandleService;
    }

//    @PostConstruct
    public void initialize() {
        try {
            stockCandleService.getStockCandles();
        } catch (Exception e) {
            // handle exception as needed
            e.printStackTrace();
        }
    }
}