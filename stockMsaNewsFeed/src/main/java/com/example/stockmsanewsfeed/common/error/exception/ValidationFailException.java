package com.example.stockmsanewsfeed.common.error.exception;


import com.example.stockmsanewsfeed.common.error.ErrorCode;
import com.example.stockmsanewsfeed.common.error.ResponseCode;
import com.example.stockmsanewsfeed.common.error.ResponseMessage;

public class ValidationFailException extends CommonException {
    public ValidationFailException(String message) {
        super(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL, message);
    }
}
