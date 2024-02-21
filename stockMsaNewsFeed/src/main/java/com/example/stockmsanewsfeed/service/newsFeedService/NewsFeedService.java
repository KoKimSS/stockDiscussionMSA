package com.example.stockmsanewsfeed.service.newsFeedService;


import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.CreateNewsFeedResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedByTypeResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;

public interface NewsFeedService {
    CreateNewsFeedResponseDto createNewsFeed(CreateNewsFeedRequestDto dto);

    GetMyNewsFeedResponseDto getMyNewsFeeds(GetMyNewsFeedRequestDto dto);

    GetMyNewsFeedByTypeResponseDto getMyNewsFeedsByType(GetMyNewsFeedByTypesRequestDto dto);
}
