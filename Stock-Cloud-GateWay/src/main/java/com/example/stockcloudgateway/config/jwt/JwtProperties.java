package com.example.stockcloudgateway.config.jwt;

public interface JwtProperties {
    String SECRET = "cos"; // 우리 서버만 알고 있는 비밀값
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
