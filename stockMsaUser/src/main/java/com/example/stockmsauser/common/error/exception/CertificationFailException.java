package com.example.stockmsauser.common.error.exception;


import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;

public class CertificationFailException extends CommonException {


    public CertificationFailException(String message) {
        super(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL, message);
    }
}
