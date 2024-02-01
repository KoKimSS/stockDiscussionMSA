package com.example.stockmsauser.service.authService;


import com.example.stockmsauser.web.dto.request.auth.CheckCertificationRequestDto;
import com.example.stockmsauser.web.dto.request.auth.EmailCertificationRequestDto;
import com.example.stockmsauser.web.dto.request.auth.EmailCheckRequestDto;
import com.example.stockmsauser.web.dto.request.auth.SignUpRequestDto;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import com.example.stockmsauser.web.dto.response.auth.CheckCertificationResponseDto;
import com.example.stockmsauser.web.dto.response.auth.EmailCertificationResponseDto;
import com.example.stockmsauser.web.dto.response.auth.EmailCheckResponseDto;
import com.example.stockmsauser.web.dto.response.auth.SignUpResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto);

    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);

    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);

    ResponseEntity<? super SignUpResponseDto> singUp(SignUpRequestDto dto);

    ResponseEntity<? super ResponseDto> logOut(String token);
}
