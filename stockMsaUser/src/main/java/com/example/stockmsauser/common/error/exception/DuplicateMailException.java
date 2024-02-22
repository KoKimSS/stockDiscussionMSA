package com.example.stockmsauser.common.error.exception;

import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;

public class DuplicateMailException extends CommonException {
    public DuplicateMailException(String message) {
        super(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL, message);
    }
}
