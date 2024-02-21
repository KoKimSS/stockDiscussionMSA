package com.example.stockmsanewsfeed.common.error.exception;

import com.example.stockmsanewsfeed.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{
    private String responseCode;
    private String responseMessage;

    public CommonException(String responseCode,String responseMessage, String message) {
        super(message);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}
