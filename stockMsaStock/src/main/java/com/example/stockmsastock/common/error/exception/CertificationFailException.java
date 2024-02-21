package com.example.stockmsastock.common.error.exception;


import com.example.stockmsastock.common.error.ResponseCode;
import com.example.stockmsastock.common.error.ResponseMessage;

public class CertificationFailException extends CommonException {

    public CertificationFailException(String message) {
        super(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL, message);
    }
}
