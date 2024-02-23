package com.example.stockmsaactivity.web.controller;

import com.example.stockmsaactivity.common.error.exception.CertificationFailException;
import com.example.stockmsaactivity.service.posterService.PosterService;
import com.example.stockmsaactivity.web.dto.request.poster.*;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static com.example.stockmsaactivity.common.jwt.JwtProperties.HEADER_STRING;
import static com.example.stockmsaactivity.common.jwt.JwtUtil.getTokenFromHeader;
import static com.example.stockmsaactivity.common.jwt.JwtUtil.getUserIdFromToken;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class PosterController {

    private final PosterService posterService;

    @PostMapping("/create-poster")
    ResponseEntity<ResponseDto<Long>> createPoster(
            @Valid@RequestBody CreatePosterRequestDto requestBody,
            HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if(loginId!=userId) throw new CertificationFailException("인증 실패");

        Long posterId = posterService.createPoster(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(posterId));
    }

    @GetMapping("/get-my-poster")
    ResponseEntity<ResponseDto<List<PosterDto>>> getMyPoster(
        @RequestBody GetMyPosterRequestDto requestBody,
        HttpServletRequest request
    ){
        String jwtToken = getTokenFromHeader(request.getHeader(HEADER_STRING));
        Long loginId = getUserIdFromToken(jwtToken);
        Long userId = requestBody.getUserId();
        if(loginId!=userId) throw new CertificationFailException("인증 실패");

        List<PosterDto> myPoster = posterService.getMyPoster(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(myPoster));
    }

    @PostMapping("/get-poster-by-id")
    ResponseEntity<ResponseDto<PosterDto>> getPosterById(
            @RequestBody GetPosterRequestDto requestBody
    ) {
        PosterDto poster = posterService.getPoster(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(poster));
    }

    @PostMapping("/get-posters-by-id-list")
    ResponseEntity<ResponseDto<List<PosterDto>>> getPostersByIdList(
            @RequestBody GetPostersByIdListRequestDto requestBody
    ) {
        List<PosterDto> posterByIdList = posterService.getPosterByIdList(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(posterByIdList));
    }

    @PostMapping("/get-posters-by-stockCode")
    ResponseEntity<ResponseDto<PosterPageDto>> getPosterPageByStockCode(
            @RequestBody GetPostersByStockCodeRequest requestBody
    ) {
        PosterPageDto posterByStockCode = posterService.getPosterByStockCode(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.ofSuccess(posterByStockCode));
    }
}
