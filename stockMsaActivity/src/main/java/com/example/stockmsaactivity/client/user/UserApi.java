package com.example.stockmsaactivity.client.user;

import com.example.stockmsaactivity.client.dto.request.user.GetUserRequestDto;
import com.example.stockmsaactivity.client.dto.response.user.GetMyFollowersResponseDto;
import com.example.stockmsaactivity.client.dto.response.user.GetUserResponseDto;
import com.example.stockmsaactivity.client.dto.request.user.GetMyFollowersRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user", url = "http://localhost:8081/api/internal/user")
public interface UserApi {

    @PostMapping(path = "/find-by-id")
    GetUserResponseDto getUserById(@RequestBody GetUserRequestDto dto);

    @PostMapping("/get-my-follower")
    GetMyFollowersResponseDto getMyFollower(GetMyFollowersRequestDto dto);
}