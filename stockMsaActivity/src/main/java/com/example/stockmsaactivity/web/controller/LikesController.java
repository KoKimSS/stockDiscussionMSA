package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.common.error.exception.CertificationFailException;
import com.example.stockmsaactivity.service.likesService.LikesService;
import com.example.stockmsaactivity.web.dto.request.likes.CreateLikesRequestDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.stockmsaactivity.common.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsaactivity.common.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsaactivity.common.jwt.JwtUtil.getUserIdFromToken;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/create-likes")
    ResponseEntity<ResponseDto<Long>> createLikes(
            @Valid @RequestBody CreateLikesRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        System.out.println(loginId +" "+ userId);
        if(loginId!=userId) throw new CertificationFailException("인증 실패");

        Long likeId = likesService.createLikes(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(likeId));
    }

}
