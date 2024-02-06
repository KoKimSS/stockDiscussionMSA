package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.service.posterService.PosterService;
import com.example.stockmsaactivity.web.dto.request.poster.GetPostersByIdListRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.CreatePosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetMyPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.CreatePosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetMyPosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPostersByIdListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.example.stockmsaactivity.config.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsaactivity.config.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsaactivity.config.jwt.JwtUtil.getUserIdFromToken;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class PosterController {

    private final PosterService posterService;

    @PostMapping("/create-poster")
    ResponseEntity<?super CreatePosterResponseDto> createPoster(
            @Valid@RequestBody CreatePosterRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if(loginId!=userId) return ResponseDto.certificationFail();

        ResponseEntity<? super CreatePosterResponseDto> response = posterService.createPoster(requestBody);
        return response;
    }

    @GetMapping("/get-my-poster")
    ResponseEntity<?super GetMyPosterResponseDto> getMyPoster(
        @RequestBody GetMyPosterRequestDto requestBody,
        HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        ResponseEntity<? super GetMyPosterResponseDto> response = posterService.getMyPoster(requestBody);
        return response;
    }

    @PostMapping("/get-poster-by-id")
    ResponseEntity<?super GetPosterResponseDto> getPosterById(
            @RequestBody GetPosterRequestDto requestBody
    ) {
        ResponseEntity<? super GetPosterResponseDto> response = posterService.getPoster(requestBody);
        return response;
    }

    @PostMapping("/get-posters-by-id-list")
    ResponseEntity<?super GetPostersByIdListResponseDto> getPostersByIdList(
            @RequestBody GetPostersByIdListRequestDto requestBody
    ) {
        ResponseEntity<? super GetPostersByIdListResponseDto> response = posterService.getPosterByIdList(requestBody);
        return response;
    }
}
