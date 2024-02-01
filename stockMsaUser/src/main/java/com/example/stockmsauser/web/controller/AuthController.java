package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.config.jwt.JwtProperties;
import com.example.stockmsauser.service.authService.AuthService;
import com.example.stockmsauser.web.dto.request.auth.*;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import com.example.stockmsauser.web.dto.response.auth.CheckCertificationResponseDto;
import com.example.stockmsauser.web.dto.response.auth.EmailCertificationResponseDto;
import com.example.stockmsauser.web.dto.response.auth.EmailCheckResponseDto;
import com.example.stockmsauser.web.dto.response.auth.SignUpResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/email-check")
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(
            @RequestBody @Valid EmailCheckRequestDto requestBody
    ) {
        ResponseEntity<? super EmailCheckResponseDto> response = authService.emailCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(
            @RequestBody @Valid EmailCertificationRequestDto requestBody
    ) {
        ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertification(requestBody);
        return response;
    }

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(
            @RequestBody @Valid CheckCertificationUserRequestDto requestBody
    ) {
        CheckCertificationRequestDto requestDto = CheckCertificationRequestDto.builder()
                .email(requestBody.getEmail())
                .certificationNumber(requestBody.getCertificationNumber())
                .certificateTime(LocalDateTime.now())
                .build();
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestDto);
        return response;
    }

    @PostMapping("/log-out")
    public ResponseEntity<? super ResponseDto> logOut(HttpServletRequest request) {
        String tokenHeader = request.getHeader(JwtProperties.HEADER_STRING);
        // 헤더 값이 없을 경우에 대한 처리
        if (tokenHeader == null || tokenHeader.isEmpty()) {
            return ResponseDto.certificationFail();
        }
        String token = tokenHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        ResponseEntity<? super ResponseDto> response = authService.logOut(token);
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        ResponseEntity<? super SignUpResponseDto> response = authService.singUp(requestBody);
        return response;
    }
}
