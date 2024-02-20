package com.example.stockmsanewsfeed.common.error.exception;

import com.example.stockmsanewsfeed.common.error.ErrorCode;

public class DatabaseErrorException extends CommonException {
    public DatabaseErrorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}

