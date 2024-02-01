package com.example.stockeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class StockEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockEurekaServerApplication.class, args);
    }

}
