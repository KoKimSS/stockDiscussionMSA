package com.example.stockmsauser.web.controller;

import com.example.stockmsauser.config.auth.PrincipalDetails;
import com.example.stockmsauser.service.followService.FollowService;
import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.GetMyFollowersResponseDto;
import com.example.stockmsauser.web.dto.response.follow.StartFollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/start-follow")
    ResponseEntity<? super StartFollowResponseDto> startFollow(
            @RequestBody @Valid StartFollowRequestDto requestBody,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Long loginId = principalDetails.getUser().getId();
        Long userId = requestBody.getFollowerId();
        if (loginId != userId) return StartFollowResponseDto.certificationFail();

        ResponseEntity<? super StartFollowResponseDto> response = followService.follow(requestBody);
        return response;
    }

    @PostMapping("/get-my-follower")
    ResponseEntity<? super GetMyFollowersResponseDto> getMyFollower(
            @RequestBody @Valid GetMyFollowersRequestDto requestBody
    ){
        ResponseEntity<? super GetMyFollowersResponseDto> response = followService.getMyFollower(requestBody);
        return response;
    }
}
