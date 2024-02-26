package com.example.stockmsanewsfeed.web.controller.circuitBreakerTest;

import com.example.stockmsanewsfeed.client.dto.request.user.GetMyFollowersRequestDto;
import com.example.stockmsanewsfeed.client.dto.request.user.GetUserRequestDto;
import com.example.stockmsanewsfeed.client.dto.response.user.FollowerDto;
import com.example.stockmsanewsfeed.client.dto.response.user.UserDto;
import com.example.stockmsanewsfeed.client.user.UserApi;
import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.stockmsanewsfeed.web.controller.circuitBreakerTest.CircuitBreakerTestController.*;

@Component
public class Fallback  implements UserApi {


    @Override
    public UserDto getUserById(GetUserRequestDto dto) {
        return null;
    }

    @Override
    public List<FollowerDto> getMyFollower(GetMyFollowersRequestDto dto) {
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