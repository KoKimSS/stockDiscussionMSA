package com.example.stockmsaactivity.client.newsFeed;

import com.example.stockmsaactivity.client.dto.CreateNewsFeedRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "newsFeed",url = "http://localhost:8083/api/internal/newsFeed/")
public interface NewsFeedApi {

    @PostMapping("/create-newsFeed")
    void createNewsFeed(CreateNewsFeedRequestDto createNewsFeedRequestDto);

}
