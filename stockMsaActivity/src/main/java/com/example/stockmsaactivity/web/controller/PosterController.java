package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.service.posterService.PosterService;
import com.example.stockmsaactivity.web.dto.request.poster.GetPostersByIdListRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.CreatePosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetMyPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.response.poster.CreatePosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetMyPosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPostersByIdListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/poster")
@RequiredArgsConstructor
public class PosterController {

    private final PosterService posterService;

    @PostMapping("/create-poster")
    ResponseEntity<?super CreatePosterResponseDto> createPoster(
//            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid@RequestBody CreatePosterRequestDto requestBody
    ){
//        Long loginId = principalDetails.getUser().getId();
//        Long userId = requestBody.getUserId();
//        if (loginId != userId) return CreatePosterResponseDto.certificationFail();

        ResponseEntity<? super CreatePosterResponseDto> response = posterService.createPoster(requestBody);
        return response;
    }

    @GetMapping("/get-my-poster")
    ResponseEntity<?super GetMyPosterResponseDto> getMyPoster(
        @RequestBody GetMyPosterRequestDto body
    ) {
        ResponseEntity<? super GetMyPosterResponseDto> response = posterService.getMyPoster(body);
        return response;
    }

    @PostMapping("/get-poster-by-id")
    ResponseEntity<?super GetPosterResponseDto> getPosterById(
            @RequestBody GetPosterRequestDto body
    ) {
        ResponseEntity<? super GetPosterResponseDto> response = posterService.getPoster(body);
        return response;
    }

    @PostMapping("/get-posters-by-id-list")
    ResponseEntity<?super GetPostersByIdListResponseDto> getPostersByIdList(
            @RequestBody GetPostersByIdListRequestDto body
    ) {
        ResponseEntity<? super GetPostersByIdListResponseDto> response = posterService.getPosterByIdList(body);
        return response;
    }
}
