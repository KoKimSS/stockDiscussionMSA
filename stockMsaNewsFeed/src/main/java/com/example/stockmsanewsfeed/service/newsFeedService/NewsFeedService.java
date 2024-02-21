package com.example.stockmsanewsfeed.service.newsFeedService;


import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.NewsFeedDto;
import org.springframework.data.domain.Page;

public interface NewsFeedService {
    void createNewsFeed(CreateNewsFeedRequestDto dto);

    Page<NewsFeedDto> getMyNewsFeeds(GetMyNewsFeedRequestDto dto);

    Page<NewsFeedDto> getMyNewsFeedsByType(GetMyNewsFeedByTypesRequestDto dto);
}
