package com.example.stockmsaactivity.common.error.exception;


import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;

public class InternalServerErrorException extends CommonException{
    public InternalServerErrorException(String message) {
        super(ResponseCode.INTERNAL_ERROR, ResponseMessage.INTERNAL_ERROR,message);
    }
}
