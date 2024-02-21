package com.example.stockmsauser.common.error.exception;


import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;

public class DatabaseErrorException extends CommonException {
    public DatabaseErrorException(String message) {
        super(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR, message);
    }
}

