package com.example.stockmsanewsfeed.common.error.exception;


import com.example.stockmsanewsfeed.common.error.ErrorCode;

public class ValidationFailException extends CommonException {
    public ValidationFailException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
