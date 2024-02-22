package com.example.stockmsauser.common.error;

public interface ResponseCode {
    String SUCCESS = "SU";
    String VALIDATION_FAIL = "VF";
    String DUPLICATE_EMAIL = "DE";
    String DUPLICATE_FOLLOW = "DF";
    String SIGN_IN_FAIL = "SF";
    String CERTIFICATION_FAIL = "CF";
    String MAIL_FAIL = "MF";
    String DATABASE_ERROR = "DBE";
    String CERTIFICATION_EXPIRED = "CE";
    String INTERNAL_ERROR = "IE";
}
