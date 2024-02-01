package com.example.stockmsanewsfeed.web.dto.response.newsFeed;


import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetMyNewsFeedByTypeResponseDto extends ResponseDto {
    private final Page<NewsFeedDto> newsFeedPage;
    public GetMyNewsFeedByTypeResponseDto(Page<NewsFeedDto> newsFeedPage) {

        this.newsFeedPage = newsFeedPage;
    }

    public static ResponseEntity<GetMyNewsFeedByTypeResponseDto> success(Page<NewsFeedDto> newsFeedPage) {
        GetMyNewsFeedByTypeResponseDto responseBody = new GetMyNewsFeedByTypeResponseDto(newsFeedPage);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
