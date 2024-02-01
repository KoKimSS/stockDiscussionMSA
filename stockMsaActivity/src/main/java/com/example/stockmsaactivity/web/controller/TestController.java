package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.web.api.dto.CreateNewsFeedRequestDto;
import com.example.stockmsaactivity.web.api.dto.response.ApiResponseDto;
import com.example.stockmsaactivity.web.api.newsFeed.NewsFeedApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final NewsFeedApi newsFeedApi;

    @GetMapping("/create-newsFeed")
    void getContributor() {
        newsFeedApi.createNewsFeed(CreateNewsFeedRequestDto.builder()
                .activityType("REPLY")
                .userId(1L)
                .relatedPosterId(3L)
                .relatedUserId(2L)
                .build());
    }
}
