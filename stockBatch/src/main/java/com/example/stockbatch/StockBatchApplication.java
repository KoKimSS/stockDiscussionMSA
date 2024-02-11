package com.example.stockbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockBatchApplication.class, args);
    }
}
