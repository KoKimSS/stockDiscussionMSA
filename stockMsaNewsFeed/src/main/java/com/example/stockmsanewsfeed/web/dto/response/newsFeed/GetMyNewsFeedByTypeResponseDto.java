package com.example.stockmsanewsfeed.web.dto.response.newsFeed;


import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetMyNewsFeedByTypeResponseDto extends ResponseDto {
    private final Page<NewsFeedDto> newsFeedPage;
    @Builder
    private GetMyNewsFeedByTypeResponseDto(Page<NewsFeedDto> newsFeedPage) {
        super();
        this.newsFeedPage = newsFeedPage;
    }
}
