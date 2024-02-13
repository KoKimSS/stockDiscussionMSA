package com.example.stockmsaactivity.service.posterService;

import com.example.stockmsaactivity.web.dto.request.poster.*;
import com.example.stockmsaactivity.web.dto.response.poster.*;
import org.springframework.http.ResponseEntity;

public interface PosterService {

    ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto);
    ResponseEntity<? super GetMyPosterResponseDto> getMyPoster(GetMyPosterRequestDto dto);
    ResponseEntity<? super GetPosterResponseDto> getPoster(GetPosterRequestDto dto);
    ResponseEntity<? super GetPostersByIdListResponseDto> getPosterByIdList(GetPostersByIdListRequestDto dto);

    ResponseEntity<? super GetPostersByStockCodeResponseDto> getPosterByStockCode(GetPostersByStockCodeRequest dto);
}
