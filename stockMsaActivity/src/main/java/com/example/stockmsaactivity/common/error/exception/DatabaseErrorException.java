package com.example.stockmsaactivity.common.error.exception;


import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;

public class DatabaseErrorException extends CommonException {
    public DatabaseErrorException(String message) {
        super(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR, message);
    }


}

