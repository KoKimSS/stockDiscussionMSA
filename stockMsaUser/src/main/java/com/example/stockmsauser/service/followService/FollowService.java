package com.example.stockmsauser.service.followService;


import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.GetMyFollowersResponseDto;
import com.example.stockmsauser.web.dto.response.follow.StartFollowResponseDto;
import org.springframework.http.ResponseEntity;

public interface FollowService {
    ResponseEntity<? super StartFollowResponseDto> follow(StartFollowRequestDto dto);
    ResponseEntity<? super GetMyFollowersResponseDto> getMyFollower(GetMyFollowersRequestDto dto);
}
