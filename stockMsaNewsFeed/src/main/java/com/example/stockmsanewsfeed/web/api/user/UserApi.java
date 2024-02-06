package com.example.stockmsanewsfeed.web.api.user;

import com.example.stockmsanewsfeed.web.api.dto.request.user.GetMyFollowersRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.request.user.GetUserRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.GetMyFollowersResponseDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.GetUserResponseDto;
import com.example.stockmsanewsfeed.web.controller.Fallback;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static com.example.stockmsanewsfeed.web.controller.CircuitBreakerTestController.test1FallbackCount;

@FeignClient(name = "user", url = "http://localhost:8081/api/internal/user")
public interface UserApi {
    @PostMapping(path = "/find-by-id")
    GetUserResponseDto getUserById(@RequestBody GetUserRequestDto dto);

    @PostMapping("/get-my-follower")
    GetMyFollowersResponseDto getMyFollower(GetMyFollowersRequestDto dto);


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