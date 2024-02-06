package com.example.stockmsauser.web.controller.internal;

import com.example.stockmsauser.service.followService.FollowService;
import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.GetMyFollowersResponseDto;
import com.example.stockmsauser.web.dto.response.follow.StartFollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/internal/user")
@RequiredArgsConstructor
public class InternalFollowController {
    private final FollowService followService;

    @PostMapping("/get-my-follower")
    ResponseEntity<? super GetMyFollowersResponseDto> getMyFollower(
            @RequestBody @Valid GetMyFollowersRequestDto requestBody
    ){
        ResponseEntity<? super GetMyFollowersResponseDto> response = followService.getMyFollower(requestBody);
        return response;
    }
}

