package com.example.stockmsanewsfeed.common.error;

public interface ValidationMessage {
    String NOT_NULL_USER = "유저 아이디는 필수 값 입니다.";
    String PAGE_MIN_VALUE_0 = "페이지의 최소 번호는 0입니다.";
    String PAGE_SIZE_POSITIVE = "페이지 사이즈는 양수여야 합니다.";
}
