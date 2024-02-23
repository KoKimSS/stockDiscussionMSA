package com.example.stockmsanewsfeed.client.dto.response;

import lombok.AllArgsConstructor;

public class ApiResponseDto {
    String code;
    String message;

    public ApiResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
