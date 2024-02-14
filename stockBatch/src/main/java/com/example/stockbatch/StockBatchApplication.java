package com.example.stockbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@Slf4j
public class StockBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockBatchApplication.class, args);
    }
}
