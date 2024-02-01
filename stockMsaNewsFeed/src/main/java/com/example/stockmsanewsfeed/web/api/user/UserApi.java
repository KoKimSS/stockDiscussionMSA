package com.example.stockmsanewsfeed.web.api.user;

import com.example.stockmsanewsfeed.web.api.dto.request.user.GetMyFollowersRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.request.user.GetUserRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.GetMyFollowersResponseDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.GetUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user", url = "http://localhost:8081/api/user")
public interface UserApi {

    @PostMapping(path = "/find-by-id")
    GetUserResponseDto getUserById(@RequestBody GetUserRequestDto dto);

    @PostMapping("/get-my-follower")
    GetMyFollowersResponseDto getMyFollower(GetMyFollowersRequestDto dto);
}