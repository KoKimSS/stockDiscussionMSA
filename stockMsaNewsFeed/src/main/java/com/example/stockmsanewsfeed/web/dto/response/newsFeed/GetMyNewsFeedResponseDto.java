package com.example.stockmsanewsfeed.web.dto.response.newsFeed;


import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetMyNewsFeedResponseDto extends ResponseDto {
    private final Page<NewsFeedDto> newsFeedPage;
    public GetMyNewsFeedResponseDto(Page<NewsFeedDto> newsFeedPage) {

        this.newsFeedPage = newsFeedPage;
    }

    public static ResponseEntity<GetMyNewsFeedResponseDto> success(Page<NewsFeedDto> newsFeedPage) {
        GetMyNewsFeedResponseDto responseBody = new GetMyNewsFeedResponseDto(newsFeedPage);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
