package com.example.stockmsauser.common.error.exception;


import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;

public class MailSendFailException extends CommonException {

    public MailSendFailException(String message) {
        super(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL, message);
    }
}
