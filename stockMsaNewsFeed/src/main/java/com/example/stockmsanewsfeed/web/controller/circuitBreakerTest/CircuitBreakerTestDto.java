package com.example.stockmsanewsfeed.web.controller.circuitBreakerTest;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CircuitBreakerTestDto {
    private String code;
    private String message;

    @Builder
    private CircuitBreakerTestDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
