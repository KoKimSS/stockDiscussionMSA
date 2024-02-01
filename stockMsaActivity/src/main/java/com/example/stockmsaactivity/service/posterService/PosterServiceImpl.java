package com.example.stockmsaactivity.service.posterService;


import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.web.api.dto.CreateNewsFeedRequestDto;
import com.example.stockmsaactivity.web.api.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.web.api.user.UserApi;
import com.example.stockmsaactivity.web.dto.request.poster.CreatePosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetMyPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPostersByIdListRequestDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PosterServiceImpl implements PosterService {

    private final PosterJpaRepository posterJpaRepository;
    private final UserApi userApi;
    private final NewsFeedApi newsFeedApi;
    @Override
    public ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto) {

        try {
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

            newsFeedApi.createNewsFeed(createNewsFeedRequestDto);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
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

        PosterDto posterDto;
        try {
            Poster poster = posterJpaRepository.findById(dto.getPosterId()).get();
            posterDto = PosterDto.builder().title(poster.getTitle())
                    .likeCount(poster.getLikeCount())
                    .ownerId(poster.getUserId())
                    .contents(poster.getContents())
                    .posterId(poster.getId()).build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return GetPosterResponseDto.databaseError();
        }

        return GetPosterResponseDto.success(posterDto);
    }

    @Override
    public ResponseEntity<? super GetPostersByIdListResponseDto> getPosterByIdList(GetPostersByIdListRequestDto dto) {
        List<PosterDto> posterDtoList = null;
        try {
            List<Poster> byOwnerId = posterJpaRepository.findAllByIdIn(dto.getPosterIdList());
            posterDtoList = byOwnerId.stream().map(
                    p -> PosterDto.builder().posterId(p.getId())
                            .title(p.getTitle())
                            .contents(p.getContents())
                            .ownerId(p.getUserId())
                            .likeCount(p.getLikeCount()).build()
            ).collect(Collectors.toList());
        }catch (Exception exception){
            exception.printStackTrace();
            ResponseDto.databaseError();
        }
        return GetPostersByIdListResponseDto.success(posterDtoList);
    }
}
