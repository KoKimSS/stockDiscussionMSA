package com.example.stockmsanewsfeed.common.error.exception;

import com.example.stockmsanewsfeed.common.error.ResponseCode;
import com.example.stockmsanewsfeed.common.error.ResponseMessage;

public class InternalServerErrorException extends CommonException{
    public InternalServerErrorException(String message) {
        super(ResponseCode.INTERNAL_ERROR, ResponseMessage.INTERNAL_ERROR,message);
    }
}
