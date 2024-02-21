package com.example.stockmsanewsfeed.web.controller;


import com.example.stockmsanewsfeed.common.error.exception.CertificationFailException;
import com.example.stockmsanewsfeed.service.newsFeedService.NewsFeedService;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.CreateNewsFeedResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedByTypeResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.stockmsanewsfeed.common.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsanewsfeed.common.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsanewsfeed.common.jwt.JwtUtil.getUserIdFromToken;

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
        if(loginId!=userId) throw new CertificationFailException("인증 오류");

        GetMyNewsFeedResponseDto responseDto = newsFeedService.getMyNewsFeeds(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    @PostMapping("/get-myNewsFeed-by-types")
    ResponseEntity<? super GetMyNewsFeedByTypeResponseDto> getMyNewsFeedByTypes(
            @Valid@RequestBody GetMyNewsFeedByTypesRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if(loginId!=userId) throw new CertificationFailException("인증 오류");

        GetMyNewsFeedByTypeResponseDto responseDto = newsFeedService.getMyNewsFeedsByType(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    @PostMapping("create-newsFeed")
    ResponseEntity<? super CreateNewsFeedResponseDto>createNewsFeed(
            @RequestBody CreateNewsFeedRequestDto requestBody
    ){
        log.info("뉴스피드 메시지 도착");
        CreateNewsFeedResponseDto responseDto = newsFeedService.createNewsFeed(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }
}
