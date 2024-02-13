package com.example.stockmsaactivity.web.dto.response.poster;

import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetPostersByStockCodeResponseDto extends ResponseDto {
    private final Page<PosterDto> posterPage;
    public GetPostersByStockCodeResponseDto(Page<PosterDto> posterPage) {
        this.posterPage = posterPage;
    }

    public static ResponseEntity<GetPostersByStockCodeResponseDto> success(Page<PosterDto> posterPage) {
        GetPostersByStockCodeResponseDto responseBody = new GetPostersByStockCodeResponseDto(posterPage);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
