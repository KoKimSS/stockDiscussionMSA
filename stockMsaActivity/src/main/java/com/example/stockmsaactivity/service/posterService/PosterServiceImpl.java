package com.example.stockmsaactivity.service.posterService;


import com.example.stockmsaactivity.client.dto.CreateNewsFeedRequestDto;
import com.example.stockmsaactivity.client.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.common.error.exception.DatabaseErrorException;
import com.example.stockmsaactivity.common.error.exception.InternalServerErrorException;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.web.dto.request.poster.*;
import com.example.stockmsaactivity.web.dto.response.poster.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosterServiceImpl implements PosterService {

    private final PosterJpaRepository posterJpaRepository;
    private final NewsFeedApi newsFeedApi;

    @Override
    public ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto) {

        Long userId = dto.getUserId();
        Poster poster = Poster.builder().title(dto.getTitle())
                .contents(dto.getContents())
                .userId(userId)
                .build();

        posterJpaRepository.save(poster);

        //나를 팔로우 하는 사람들의 뉴스피드 업데이트
        //뉴스피드 생성 서비스 호출 !
        CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                .activityType("활동 타입")
                .build();

        try {
            newsFeedApi.createNewsFeed(createNewsFeedRequestDto);
        } catch (Exception e) {
            throw new InternalServerErrorException("internal server error");
        }

        return CreatePosterResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetMyPosterResponseDto> getMyPoster(GetMyPosterRequestDto dto) {
        List<Poster> byOwnerId = posterJpaRepository.findAllByUserId(dto.getUserId());

        List<PosterDto> posterDtoList = byOwnerId.stream().map(
                p -> PosterDto.builder().posterId(p.getId())
                        .title(p.getTitle())
                        .contents(p.getContents())
                        .ownerId(p.getUserId())
                        .likeCount(p.getLikeCount()).build()
        ).collect(Collectors.toList());

        return GetMyPosterResponseDto.success(posterDtoList);
    }

    @Override
    public ResponseEntity<? super GetPosterResponseDto> getPoster(GetPosterRequestDto dto) {

        Poster poster = posterJpaRepository.findById(dto.getPosterId())
                .orElseThrow(() -> new DatabaseErrorException("db 에러"));

        PosterDto posterDto = PosterDto.builder().title(poster.getTitle())
                .likeCount(poster.getLikeCount())
                .ownerId(poster.getUserId())
                .contents(poster.getContents())
                .posterId(poster.getId()).build();

        return GetPosterResponseDto.success(posterDto);
    }

    @Override
    public ResponseEntity<? super GetPostersByIdListResponseDto> getPosterByIdList(GetPostersByIdListRequestDto dto) {
        List<Poster> byOwnerId = posterJpaRepository.findAllByIdIn(dto.getPosterIdList());

        List<PosterDto> posterDtoList = byOwnerId.stream().map(
                p -> PosterDto.builder().posterId(p.getId())
                        .title(p.getTitle())
                        .contents(p.getContents())
                        .ownerId(p.getUserId())
                        .likeCount(p.getLikeCount()).build()
        ).collect(Collectors.toList());
        return GetPostersByIdListResponseDto.success(posterDtoList);
    }

    @Override
    public ResponseEntity<? super GetPostersByStockCodeResponseDto> getPosterByStockCode(GetPostersByStockCodeRequest dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        Page<Poster> posterByStockCode = posterJpaRepository.findPosterByStockCode(dto.getStockCode(), pageable);
        Page<PosterDto> posterDtoByStockCode = posterByStockCode.map(poster -> PosterDto.builder()
                .stockCode(poster.getStockCode())
                .posterId(poster.getId())
                .title(poster.getTitle())
                .contents(poster.getContents())
                .likeCount(poster.getLikeCount())
                .ownerId(poster.getUserId()).build()
        );

        return GetPostersByStockCodeResponseDto.success(posterDtoByStockCode);
    }
}
