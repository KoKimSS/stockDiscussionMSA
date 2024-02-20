package com.example.stockmsanewsfeed.common.error;

public enum ErrorCode {
    INVALID_INPUT_VALUE("COMMON-001", "유효성 검증에 실패한 경우"),
    FILE_UPLOAD_CONFLICT("COMMON-002", "파일 업로드 중 실패한 경우");


    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
