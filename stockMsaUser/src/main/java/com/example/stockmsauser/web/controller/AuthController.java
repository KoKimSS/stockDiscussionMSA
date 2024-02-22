package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.common.error.exception.CertificationFailException;
import com.example.stockmsauser.config.jwt.JwtProperties;
import com.example.stockmsauser.config.jwt.JwtUtil;
import com.example.stockmsauser.service.authService.AuthService;
import com.example.stockmsauser.web.dto.request.auth.*;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseDto<Boolean>> emailCheck(
            @RequestBody @Valid EmailCheckRequestDto requestBody
    ) {
        boolean emailCheck = authService.emailCheck(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(emailCheck));
    }

    @PostMapping("/email-certification")
    public ResponseEntity<ResponseDto<Boolean>> emailCertification(
            @RequestBody @Valid EmailCertificationRequestDto requestBody
    ) {
        boolean isSuccess = authService.emailCertification(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(isSuccess));
    }

    @PostMapping("/check-certification")
    public ResponseEntity<ResponseDto<Boolean>> checkCertification(
            @RequestBody @Valid CheckCertificationUserRequestDto requestBody
    ) {
        CheckCertificationRequestDto requestDto = CheckCertificationRequestDto.builder()
                .email(requestBody.getEmail())
                .certificationNumber(requestBody.getCertificationNumber())
                .certificateTime(LocalDateTime.now())
                .build();
        boolean isSuccess = authService.checkCertification(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(isSuccess));
    }

    @PostMapping("/log-out")
    public ResponseEntity<ResponseDto<Boolean>> logOut(HttpServletRequest request) {
        String tokenHeader = request.getHeader(JwtProperties.HEADER_STRING);
        // 헤더 값이 없을 경우에 대한 처리
        if (tokenHeader == null || tokenHeader.isEmpty()) {
            throw new CertificationFailException("인증 실패");
        }
        String token = JwtUtil.getTokenFromHeader(tokenHeader);
        if(token==null) throw new CertificationFailException("인증 실패");

        boolean isSuccess = authService.logOut(token);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(isSuccess));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<Long>> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        Long userId = authService.singUp(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(userId));
    }
}
