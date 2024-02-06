package com.example.stockmsaactivity.service.posterService;

import com.example.stockmsaactivity.common.ResponseCode;
import com.example.stockmsaactivity.common.ResponseMessage;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.web.api.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.web.dto.request.poster.CreatePosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetMyPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.response.poster.CreatePosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetMyPosterResponseDto;
import com.example.stockmsaactivity.web.dto.response.poster.GetPosterResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PosterServiceTest {

    @Autowired
    private PosterJpaRepository posterJpaRepository;
    @Autowired
    private PosterService posterService;
    @MockBean
    private NewsFeedApi newsFeedApi;

    @DisplayName("포스터를 생성하는 서비스 이다")
    @Test
    public void createPoster() throws Exception {
        //given
        Long userId = 1L;
        String title = "title";
        String contents = "contents";
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.
                builder().title(title).contents(contents).userId(userId).build();

        //when
        ResponseEntity<? super CreatePosterResponseDto> response = posterService.createPoster(requestDto);

        //then
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @DisplayName("나의 포스터 가져오기")
    @Test
    public void getMyPoster() throws Exception {
        //given
        Long userId = 1L;
        String title = "title";
        String contents = "contents";
        Poster poster = Poster.builder().userId(userId).contents(contents).title(title).build();
        posterJpaRepository.save(poster);

        GetMyPosterRequestDto requestDto = GetMyPosterRequestDto.
                builder().userId(userId).build();
        //when
        ResponseEntity<? super GetMyPosterResponseDto> response = posterService.getMyPoster(requestDto);
        GetMyPosterResponseDto responseBody = (GetMyPosterResponseDto)response.getBody();

        //then
        Assertions.assertThat(responseBody.getPosterList())
                .extracting("ownerId")
                .containsExactly(1L);

        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @DisplayName("아이디로 포스터 가져오기")
    @Test
    public void GetPoster() throws Exception {
        //given
        Long userId = 1L;
        String title = "title";
        String contents = "contents";
        Poster poster = Poster.builder().userId(userId).contents(contents).title(title).build();
        Poster save = posterJpaRepository.save(poster);
        Long posterId = save.getId();

        GetPosterRequestDto requestDto = GetPosterRequestDto.
                builder().posterId(posterId).build();
        //when
        ResponseEntity<? super GetPosterResponseDto> response = posterService.getPoster(requestDto);
        GetPosterResponseDto body = (GetPosterResponseDto) response.getBody();

        //then
        Assertions.assertThat(body.getPoster())
                .extracting("title", "contents")
                .containsExactly(title,contents);

        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

}