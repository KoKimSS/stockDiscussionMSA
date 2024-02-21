package com.example.stockmsaactivity.web.controller.internal;

import com.example.stockmsaactivity.service.posterService.PosterService;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPostersByIdListRequestDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.PosterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/internal/activity")
@RequiredArgsConstructor
public class InternalPosterController {

    private final PosterService posterService;

    @PostMapping("/get-poster-by-id")
    ResponseEntity getPosterById(
            @RequestBody GetPosterRequestDto requestBody
    ) {
        PosterDto poster = posterService.getPoster(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(poster));
    }

    @PostMapping("/get-posters-by-id-list")
    ResponseEntity getPostersByIdList(
            @RequestBody GetPostersByIdListRequestDto requestBody
    ) {
        List<PosterDto> posterByIdList = posterService.getPosterByIdList(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(posterByIdList));
    }
}
