package com.example.stockmsauser.web.api.newsFeed;

import com.example.stockmsauser.web.api.newsFeed.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsauser.web.api.newsFeed.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "newsFeed",url = "https://localhost:8083/api/internal/newsfeed")
public interface NewsFeedApi {
    @PostMapping("/create-newsFeed")
    ApiResponseDto createNewsFeed(CreateNewsFeedRequestDto createNewsFeedRequestDto);
}
