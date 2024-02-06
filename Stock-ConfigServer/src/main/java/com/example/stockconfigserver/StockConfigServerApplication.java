package com.example.stockconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableConfigServer
@PropertySource("classpath:env.properties")
public class StockConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockConfigServerApplication.class, args);
    }
}
