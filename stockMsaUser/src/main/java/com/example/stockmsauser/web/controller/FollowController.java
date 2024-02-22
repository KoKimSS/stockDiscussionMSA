package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.common.error.exception.CertificationFailException;
import com.example.stockmsauser.service.followService.FollowService;
import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import com.example.stockmsauser.web.dto.response.follow.FollowerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static com.example.stockmsauser.config.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsauser.config.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsauser.config.jwt.JwtUtil.getUserIdFromToken;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/start-follow")
    ResponseEntity<ResponseDto<Boolean>> startFollow(
            @RequestBody @Valid StartFollowRequestDto requestBody,
            HttpServletRequest request
    ) {
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getFollowerId();
        if (loginId != userId) throw new CertificationFailException("인증 실패");

        boolean isSuccess = followService.follow(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(isSuccess));
    }

    @PostMapping("/get-my-follower")
    ResponseEntity<ResponseDto<List<FollowerDto>>> getMyFollower(
            @RequestBody @Valid GetMyFollowersRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if (loginId != userId) throw new CertificationFailException("인증 실패");

        List<FollowerDto> myFollower = followService.getMyFollower(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(myFollower));
    }
}
