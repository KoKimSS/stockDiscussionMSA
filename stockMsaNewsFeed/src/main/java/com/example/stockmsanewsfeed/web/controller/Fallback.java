package com.example.stockmsanewsfeed.web.controller;

import com.example.stockmsanewsfeed.web.api.dto.request.user.GetMyFollowersRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.request.user.GetUserRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.GetMyFollowersResponseDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.GetUserResponseDto;
import com.example.stockmsanewsfeed.web.api.user.UserApi;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.example.stockmsanewsfeed.web.controller.CircuitBreakerTestController.*;

@Component
public class Fallback  implements UserApi {


    @Override
    public GetUserResponseDto getUserById(GetUserRequestDto dto) {
        return null;
    }

    @Override
    public GetMyFollowersResponseDto getMyFollower(GetMyFollowersRequestDto dto) {
        return null;
    }

    @Override
    public String test1Circuit() {
        test1FallbackCount++;
        return "fallback1";
    }

    @Override
    public String test2Circuit() {
        test2FallbackCount++;

        return "fallback2";
    }

    @Override
    public String test3Circuit() {
        test3FallbackCount++;

        return "fallback3";
    }
}