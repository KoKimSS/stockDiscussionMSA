package com.example.stockmsauser.client.api.newsFeed;

import com.example.stockmsauser.client.api.newsFeed.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsauser.client.api.newsFeed.response.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "newsFeed",url = "http://localhost:8083/api/internal/newsFeed")
public interface NewsFeedApi {
    @PostMapping("/create-newsFeed")
    ApiResponseDto createNewsFeed(CreateNewsFeedRequestDto createNewsFeedRequestDto);
}
