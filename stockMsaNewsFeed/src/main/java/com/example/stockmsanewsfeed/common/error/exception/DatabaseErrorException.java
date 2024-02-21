package com.example.stockmsanewsfeed.common.error.exception;

import com.example.stockmsanewsfeed.common.error.ResponseCode;
import com.example.stockmsanewsfeed.common.error.ResponseMessage;

public class DatabaseErrorException extends CommonException {
    public DatabaseErrorException(String message) {
        super(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR, message);
    }
}

