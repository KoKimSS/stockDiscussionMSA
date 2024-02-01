package com.example.stockmsanewsfeed.web.controller;


import com.example.stockmsanewsfeed.service.newsFeedService.NewsFeedService;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.CreateNewsFeedResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedByTypeResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/newsFeed")
@RequiredArgsConstructor
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    @PostMapping("/get-myNewsFeed")
    ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeed(
//            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid@RequestBody GetMyNewsFeedRequestDto requestBody
    ) {
//        Long loginUserId = principalDetails.getUser().getId();
//        Long requestUserId = requestBody.getUserId();
//        if(loginUserId != requestUserId) return ApiResponseDto.certificationFail();

        ResponseEntity<? super GetMyNewsFeedResponseDto> response = newsFeedService.getMyNewsFeeds(requestBody);
        return response;
    }

    @PostMapping("/get-myNewsFeed-by-types")
    ResponseEntity<? super GetMyNewsFeedByTypeResponseDto> getMyNewsFeedByTypes(
//            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid@RequestBody GetMyNewsFeedByTypesRequestDto requestBody
    ) {
//        Long loginUserId = principalDetails.getUser().getId();
//        Long requestUserId = requestBody.getUserId();
//        if(loginUserId != requestUserId) return ApiResponseDto.certificationFail();

        ResponseEntity<? super GetMyNewsFeedByTypeResponseDto> response = newsFeedService.getMyNewsFeedsByType(requestBody);
        return response;
    }

    @PostMapping("create-newsFeed")
    ResponseEntity<? super CreateNewsFeedResponseDto>createNewsFeed(
            @RequestBody CreateNewsFeedRequestDto requestBody
    ){
        log.info("뉴스피드 메시지 도착");
        ResponseEntity<? super CreateNewsFeedResponseDto> response = newsFeedService.createNewsFeed(requestBody);
        return response;
    }


}
