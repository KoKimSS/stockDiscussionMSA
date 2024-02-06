package com.example.stockmsanewsfeed.web.controller;


import com.example.stockmsanewsfeed.service.newsFeedService.NewsFeedService;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.CreateNewsFeedResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedByTypeResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.stockmsanewsfeed.config.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsanewsfeed.config.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsanewsfeed.config.jwt.JwtUtil.getUserIdFromToken;

@Slf4j
@RestController
@RequestMapping("/api/newsFeed")
@RequiredArgsConstructor
public class NewsFeedController {
    private final NewsFeedService newsFeedService;

    @PostMapping("/get-myNewsFeed")
    ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeed(
            @Valid@RequestBody GetMyNewsFeedRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if(loginId!=userId) return ResponseDto.certificationFail();
        ResponseEntity<? super GetMyNewsFeedResponseDto> response = newsFeedService.getMyNewsFeeds(requestBody);
        return response;
    }

    @PostMapping("/get-myNewsFeed-by-types")
    ResponseEntity<? super GetMyNewsFeedByTypeResponseDto> getMyNewsFeedByTypes(
            @Valid@RequestBody GetMyNewsFeedByTypesRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
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
