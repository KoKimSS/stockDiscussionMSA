package com.example.stockmsauser.common.error.exception;

import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;

public class CertificationExpiredException extends CommonException {

    public CertificationExpiredException(String message) {
        super(ResponseCode.CERTIFICATION_EXPIRED, ResponseMessage.CERTIFICATION_EXPIRED, message);
    }
}
