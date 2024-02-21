package com.example.stockmsastock.common.error.exception;


import com.example.stockmsastock.common.error.ResponseCode;
import com.example.stockmsastock.common.error.ResponseMessage;

public class DatabaseErrorException extends CommonException {
    public DatabaseErrorException(String message) {
        super(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR, message);
    }


}

