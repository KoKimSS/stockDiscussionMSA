package com.example.stockmsauser.common.error.exception;


import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;

public class InternalServerErrorException extends CommonException{
    public InternalServerErrorException(String message) {
        super(ResponseCode.INTERNAL_ERROR, ResponseMessage.INTERNAL_ERROR,message);
    }
}
