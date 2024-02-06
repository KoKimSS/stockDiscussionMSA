package com.example.stockmsanewsfeed.web.controller;

import com.example.stockmsanewsfeed.web.api.user.UserApi;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@RequiredArgsConstructor
public class CircuitBreakerTestController {
    private final UserApi userApi;
    static int test1Count = 0;
    public static int test1FallbackCount = 0;
    static int test2Count = 0;
    static int test2FallbackCount = 0;
    static int test3Count = 0;
    static int test3FallbackCount = 0;

    @Scheduled(fixedRate = 500) // 0.5초에 한 번씩 실행
    String startCircuit1() {
        test1Count++;
        String string = userApi.test1Circuit();
        System.out.println("Test1 Result: " + string);
        System.out.println("test1Count = " + test1Count);
        System.out.println("test1FallbackCount = " + test1FallbackCount);
        return string;
    }
//    @Scheduled(fixedRate = 1000) // 1초에 한 번씩 실행
    String startCircuit2() {
        test2Count++;
        String string = userApi.test2Circuit();
        System.out.println("Test2 Result: " + string);
        System.out.println("test2Count = " + test2Count);
        System.out.println("test2FallbackCount = " + test2FallbackCount);
        return string;
    }
//    @Scheduled(fixedRate = 1000) // 1초에 한 번씩 실행
    String startCircuit3() {
        test3Count++;
        String string = userApi.test3Circuit();
        System.out.println("Test3 Result: " + string);
        System.out.println("test3Count = " + test3Count);
        System.out.println("test3FallbackCount = " + test3FallbackCount);
        return string;
    }

}
