package com.example.stockmsanewsfeed.web.controller.internal;

import com.example.stockmsanewsfeed.service.newsFeedService.NewsFeedService;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.CreateNewsFeedResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/internal/newsFeed")
@RequiredArgsConstructor

public class InternalNewsFeedController {
    private final NewsFeedService newsFeedService;

    @PostMapping("create-newsFeed")
    ResponseEntity<? super CreateNewsFeedResponseDto> createNewsFeed(
            @RequestBody CreateNewsFeedRequestDto requestBody
    ){
        newsFeedService.createNewsFeed(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(null));
    }
}
