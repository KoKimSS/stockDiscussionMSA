package com.example.stockmsauser.common.error.exception;

import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;

public class DuplicateFollowException extends CommonException {
    public DuplicateFollowException(String message) {
        super(ResponseCode.DUPLICATE_FOLLOW, ResponseMessage.DUPLICATE_FOLLOW, message);
    }
}
