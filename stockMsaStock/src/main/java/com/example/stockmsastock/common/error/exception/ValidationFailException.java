package com.example.stockmsastock.common.error.exception;


import com.example.stockmsastock.common.error.ResponseCode;
import com.example.stockmsastock.common.error.ResponseMessage;

public class ValidationFailException extends CommonException {
    public ValidationFailException(String message) {
        super(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL, message);
    }
}
