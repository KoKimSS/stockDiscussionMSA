package com.example.stockmsaactivity.service.posterService;

import com.example.stockmsaactivity.web.dto.request.poster.CreatePosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetMyPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPostersByIdListRequestDto;
import com.example.stockmsaactivity.web.dto.response.poster.CreatePosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetMyPosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPostersByIdListResponseDto;
import org.springframework.http.ResponseEntity;

public interface PosterService {

    ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto);
    ResponseEntity<? super GetMyPosterResponseDto> getMyPoster(GetMyPosterRequestDto dto);
    ResponseEntity<? super GetPosterResponseDto> getPoster(GetPosterRequestDto dto);
    ResponseEntity<? super GetPostersByIdListResponseDto> getPosterByIdList(GetPostersByIdListRequestDto dto);
}
