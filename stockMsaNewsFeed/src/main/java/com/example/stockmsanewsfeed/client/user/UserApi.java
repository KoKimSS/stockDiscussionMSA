package com.example.stockmsanewsfeed.client.user;

import com.example.stockmsanewsfeed.client.dto.request.user.GetMyFollowersRequestDto;
import com.example.stockmsanewsfeed.client.dto.request.user.GetUserRequestDto;
import com.example.stockmsanewsfeed.client.dto.response.user.FollowerDto;
import com.example.stockmsanewsfeed.client.dto.response.user.GetMyFollowersResponseDto;
import com.example.stockmsanewsfeed.client.dto.response.user.GetUserResponseDto;
import com.example.stockmsanewsfeed.client.dto.response.user.UserDto;
import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.stockmsanewsfeed.web.controller.circuitBreakerTest.CircuitBreakerTestController.test1FallbackCount;

@FeignClient(name = "user", url = "http://localhost:8081/api/internal/user")
public interface UserApi {
    @PostMapping(path = "/find-by-id")
    UserDto getUserById(@RequestBody GetUserRequestDto dto);

    @PostMapping("/get-my-follower")
    List<FollowerDto> getMyFollower(GetMyFollowersRequestDto dto);


    @CircuitBreaker(name = "default", fallbackMethod = "test1Fallback")
    @GetMapping("/errorful/case1")
    String test1Circuit();

    @GetMapping("/errorful/case2")
    String test2Circuit();

    @GetMapping("/errorful/case3")
    String test3Circuit();

    default String test1Fallback(Throwable e) {
        System.out.println("fallback 실행");
        test1FallbackCount++;
        return "fallback1";
    }
}