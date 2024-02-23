package com.example.stockmsaactivity.service.replyService;

import com.example.stockmsaactivity.client.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.web.dto.request.reply.CreateReplyRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import javax.transaction.Transactional;


@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    PosterJpaRepository posterJpaRepository;

    @MockBean
    NewsFeedApi newsFeedApi;

    @DisplayName("댓글을 생성하는 서비스")
    @Test
    public void createReply() throws Exception {
        //given
        Long userId = 1L;
        Poster poster = Poster.builder().userId(userId).title("poster").build();
        posterJpaRepository.save(poster);

        CreateReplyRequestDto requestDto = CreateReplyRequestDto.builder()
                .userId(userId)
                .posterId(poster.getId())
                .contents("댓글")
                .build();

        //when
        Long replyId = replyService.createReply(requestDto);

        //then
        Assertions.assertThat(replyId)
                .isNotNull();
    }

}