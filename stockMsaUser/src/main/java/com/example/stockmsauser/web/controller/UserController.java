package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.common.error.exception.CertificationFailException;
import com.example.stockmsauser.service.userService.UserService;
import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdatePasswordRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdateProfileRequestDto;
import com.example.stockmsauser.web.dto.response.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<Long> updatePassword(
            @RequestBody@Valid UpdatePasswordRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if (loginId != userId) throw new CertificationFailException("인증 실패");

        Long updatedUserId = userService.updatePassword(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedUserId);
    }

    @PostMapping("/update-profile")
    ResponseEntity<Long> updateProfile(
            @RequestBody@Valid UpdateProfileRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if (loginId != userId) throw new CertificationFailException("인증 실패");

        Long updatedUserId = userService.updateProfile(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedUserId);
    }

    @PostMapping("/find-by-id")
    ResponseEntity<UserDto> findById(
            @RequestBody@Valid GetUserRequestDto requestBody
    ){
        UserDto userDto = userService.findById(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDto);
    }
}
