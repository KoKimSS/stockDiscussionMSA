package com.example.stockmsauser.service.authService;


import com.example.stockmsauser.web.dto.request.auth.CheckCertificationRequestDto;
import com.example.stockmsauser.web.dto.request.auth.EmailCertificationRequestDto;
import com.example.stockmsauser.web.dto.request.auth.EmailCheckRequestDto;
import com.example.stockmsauser.web.dto.request.auth.SignUpRequestDto;

import javax.transaction.Transactional;

public interface AuthService {
    boolean emailCheck(EmailCheckRequestDto dto);

    boolean emailCertification(EmailCertificationRequestDto dto);

    boolean checkCertification(CheckCertificationRequestDto dto);

    Long singUp(SignUpRequestDto dto);

    boolean logOut(String token);

    @Transactional
    void addJwtTokenToBlackListRedis(String token, long expirationMillis);
}
