package com.example.stockmsanewsfeed.common.error.exception;

import com.example.stockmsanewsfeed.common.error.ErrorCode;

public class CommonException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

    public CommonException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
