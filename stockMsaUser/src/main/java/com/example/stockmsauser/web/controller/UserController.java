package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.config.auth.PrincipalDetails;
import com.example.stockmsauser.service.userService.UserService;
import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdatePasswordRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdateProfileRequestDto;
import com.example.stockmsauser.web.dto.response.user.GetUserResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdatePasswordResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdateProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.stockmsauser.config.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsauser.config.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsauser.config.jwt.JwtUtil.getUserIdFromToken;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/update-password")
    ResponseEntity<? super UpdatePasswordResponseDto> updatePassword(
            @RequestBody@Valid UpdatePasswordRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        System.out.println(loginId+" "+userId);
        if (loginId != userId) return UpdatePasswordResponseDto.certificationFail();

        ResponseEntity<? super UpdatePasswordResponseDto> response = userService.updatePassword(requestBody);
        return response;
    }

    @PostMapping("/update-profile")
    ResponseEntity<? super UpdateProfileResponseDto> updateProfile(
            @RequestBody@Valid UpdateProfileRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();

        ResponseEntity<? super UpdateProfileResponseDto> response = userService.updateProfile(requestBody);
        return response;
    }

    @PostMapping("/find-by-id")
    ResponseEntity<? super GetUserResponseDto> findById(
            @RequestBody@Valid GetUserRequestDto requestBody
    ){
        ResponseEntity<? super GetUserResponseDto> response = userService.findById(requestBody);
        return response;
    }
}
