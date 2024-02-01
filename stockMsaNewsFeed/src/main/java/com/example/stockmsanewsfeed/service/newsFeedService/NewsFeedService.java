package com.example.stockmsanewsfeed.service.newsFeedService;


import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.CreateNewsFeedResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedByTypeResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import org.springframework.http.ResponseEntity;

public interface NewsFeedService {
    ResponseEntity<? super CreateNewsFeedResponseDto> createNewsFeed(CreateNewsFeedRequestDto dto);

    ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeeds(GetMyNewsFeedRequestDto dto);

    ResponseEntity<? super GetMyNewsFeedByTypeResponseDto> getMyNewsFeedsByType(GetMyNewsFeedByTypesRequestDto dto);
}
