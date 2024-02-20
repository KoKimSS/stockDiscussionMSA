package com.example.stockmsanewsfeed.common.error.exception;

import com.example.stockmsanewsfeed.common.error.ErrorCode;

public class CertificationFailException extends CommonException {

    public CertificationFailException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
