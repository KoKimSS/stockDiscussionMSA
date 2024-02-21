package com.example.stockmsanewsfeed.web.dto.response.newsFeed;


import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class GetMyNewsFeedResponseDto extends ResponseDto {
    private final Page<NewsFeedDto> newsFeedPage;

    @Builder
    private GetMyNewsFeedResponseDto(Page<NewsFeedDto> newsFeedPage) {
        super();
        this.newsFeedPage = newsFeedPage;
    }
}
