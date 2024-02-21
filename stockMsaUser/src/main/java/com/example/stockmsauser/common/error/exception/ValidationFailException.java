package com.example.stockmsauser.common.error.exception;


import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;

public class ValidationFailException extends CommonException {
    public ValidationFailException(String message) {
        super(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL, message);
    }
}
