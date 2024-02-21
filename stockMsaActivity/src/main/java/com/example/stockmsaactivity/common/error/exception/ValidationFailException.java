package com.example.stockmsaactivity.common.error.exception;


import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;

public class ValidationFailException extends CommonException {
    public ValidationFailException(String message) {
        super(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL, message);
    }
}
