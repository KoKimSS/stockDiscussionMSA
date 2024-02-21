package com.example.stockmsanewsfeed.common.error.exception;

import com.example.stockmsanewsfeed.common.error.ErrorCode;
import com.example.stockmsanewsfeed.common.error.ResponseCode;
import com.example.stockmsanewsfeed.common.error.ResponseMessage;

public class CertificationFailException extends CommonException {


    public CertificationFailException(String message) {
        super(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL, message);
    }
}
