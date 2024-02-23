package com.example.stockmsaactivity.client.dto.response;

public class ApiResponseDto {
    String code;
    String message;

    public ApiResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
