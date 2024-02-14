package com.example.stockbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@Slf4j
public class StockBatchApplication {
    @Value("${spring.datasource.url}")
    private static String message;
    public static void main(String[] args) {
        log.info("url은 {}",message);

        SpringApplication.run(StockBatchApplication.class, args);
    }
}
