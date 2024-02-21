package com.example.stockmsauser.common.error;

public interface ValidationMessage {
    String NOT_EMAIL = "이메일 형식에 맞지 않습니다.";
    String NOT_BLANK_EMAIL = "이메일은 필수 값 입니다.";
    String NOT_BLANK_TOKEN = "토큰은 필수 값 입니다.";
    String NOT_BLANK_NAME = "이름은 필수 값 입니다.";
    String NOT_BLANK_IMAGE = "이미지는 필수 값 입니다.";
    String NOT_BLANK_INTRO = "자기소개는 필수 값 입니다.";
    String NOT_NULL_USER = "유저 아이디는 필수 값 입니다.";
    String NOT_PASSWORD = "비밀번호는 형식에 맞지 않습니다.";
    String NOT_NULL_FOLLOWER = "팔로워 아이디는 필수 값 입니다.";
    String NOT_NULL_FOLLOWING = "팔로잉 아이디는 필수 값 입니다.";
    String NOT_BLANK_PASSWORD = "비밀번호는 필수 값 입니다.";
}
