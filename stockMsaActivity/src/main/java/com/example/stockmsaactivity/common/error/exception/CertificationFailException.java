package com.example.stockmsaactivity.common.error.exception;


import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;

public class CertificationFailException extends CommonException {

    public CertificationFailException(String message) {
        super(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL, message);
    }
}
