package com.example.stockmsaactivity.web.api.newsFeed;

import com.example.stockmsaactivity.web.api.dto.CreateNewsFeedRequestDto;
import com.example.stockmsaactivity.web.api.dto.response.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@FeignClient(name = "newsFeed",url = "http://localhost:8083/api/internal/newsFeed/")
public interface NewsFeedApi {

    @PostMapping("/create-newsFeed")
    void createNewsFeed(CreateNewsFeedRequestDto createNewsFeedRequestDto);

}
