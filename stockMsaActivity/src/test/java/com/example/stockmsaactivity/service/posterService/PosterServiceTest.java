package com.example.stockmsaactivity.service.posterService;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.kafka.KafkaProducer;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.client.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.web.dto.request.poster.CreatePosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetMyPosterRequestDto;
import com.example.stockmsaactivity.web.dto.request.poster.GetPosterRequestDto;
import com.example.stockmsaactivity.web.dto.response.poster.PosterDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
class PosterServiceTest {

    @Autowired
    private PosterJpaRepository posterJpaRepository;
    @Autowired
    private PosterService posterService;
    @MockBean
    private KafkaProducer kafkaProducer;

    @AfterEach
    void afterEach() {
        posterJpaRepository.deleteAllInBatch();

    }

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
        Long posterId = posterService.createPoster(requestDto);

        //then
        Assertions.assertThat(posterId)
                .isNotNull();
    }

    @DisplayName("나의 포스터 가져오기")
    @Test
    public void getMyPoster() throws Exception {
        //given
        Long userId = 1L;
        String title1 = "title1";
        String title2 = "title2";
        String contents1 = "contents1";
        String contents2 = "contents2";
        Poster poster1 = Poster.builder().userId(userId).contents(contents1).title(title1).build();
        Poster poster2 = Poster.builder().userId(userId).contents(contents2).title(title2).build();
        posterJpaRepository.saveAll(List.of(poster1,poster2));

        GetMyPosterRequestDto requestDto = GetMyPosterRequestDto.
                builder().userId(userId).build();
        //when
        List<PosterDto> posterDtoList = posterService.getMyPoster(requestDto);

        //then
        Assertions.assertThat(posterDtoList)
                .extracting("ownerId","title")
                .containsExactly(
                        tuple(poster1.getUserId(),poster1.getTitle()),
                        tuple(poster2.getUserId(),poster2.getTitle())
                );
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
        PosterDto posterDto = posterService.getPoster(requestDto);

        //then
        Assertions.assertThat(posterDto)
                .extracting("title", "contents")
                .containsExactly(title,contents);
    }
}