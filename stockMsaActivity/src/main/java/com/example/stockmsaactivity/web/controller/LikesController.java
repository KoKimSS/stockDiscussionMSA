package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.service.likesService.LikesService;
import com.example.stockmsaactivity.web.dto.request.likes.CreateLikesRequestDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import com.example.stockmsaactivity.web.dto.response.likes.CreateLikesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.stockmsaactivity.config.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsaactivity.config.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsaactivity.config.jwt.JwtUtil.getUserIdFromToken;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/create-likes")
    ResponseEntity<? super CreateLikesResponseDto> createLikes(
            @Valid @RequestBody CreateLikesRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        System.out.println(jwtToken);
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        System.out.println(loginId+" "+userId);

        if(loginId!=userId) return ResponseDto.certificationFail();

        ResponseEntity<? super CreateLikesResponseDto> response = likesService.createLikes(requestBody);
        return response;
    }

}
