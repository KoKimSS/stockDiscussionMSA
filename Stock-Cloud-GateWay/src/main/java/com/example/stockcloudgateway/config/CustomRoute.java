package com.example.stockcloudgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomRoute {
    @Bean
    public RouteLocator ms1Route(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("ms1", r -> r.path("/api/activity/**")
                        .uri("http://localhost:8081"))
                .route("ms2", r -> r.path("/api/user/**")
                        .uri("http://localhost:8082"))
                .build();
    }
}