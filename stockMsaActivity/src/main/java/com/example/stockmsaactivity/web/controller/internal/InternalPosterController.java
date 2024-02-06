package com.example.stockmsaactivity.web.controller.internal;

import com.example.stockmsaactivity.service.posterService.PosterService;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPostersByIdListRequestDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPostersByIdListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/activity")
@RequiredArgsConstructor
public class InternalPosterController {

    private final PosterService posterService;

    @PostMapping("/get-poster-by-id")
    ResponseEntity<? super GetPosterResponseDto> getPosterById(
            @RequestBody GetPosterRequestDto body
    ) {
        ResponseEntity<? super GetPosterResponseDto> response = posterService.getPoster(body);
        return response;
    }

    @PostMapping("/get-posters-by-id-list")
    ResponseEntity<? super GetPostersByIdListResponseDto> getPostersByIdList(
            @RequestBody GetPostersByIdListRequestDto body
    ) {
        ResponseEntity<? super GetPostersByIdListResponseDto> response = posterService.getPosterByIdList(body);
        return response;
    }
}
