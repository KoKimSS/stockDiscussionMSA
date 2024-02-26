package com.example.stockmsaactivity.service.likesService;


import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.config.TestRedisConfig;
import com.example.stockmsaactivity.domain.like.LikeType;
import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.domain.reply.Reply;
import com.example.stockmsaactivity.kafka.KafkaProducer;
import com.example.stockmsaactivity.repository.likeRepository.LikesJpaRepository;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import com.example.stockmsaactivity.repository.replyRepository.ReplyJpaRepository;
import com.example.stockmsaactivity.client.newsFeed.NewsFeedApi;
import com.example.stockmsaactivity.web.dto.request.likes.CreateLikesRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;


@SpringBootTest
@Transactional
@Import(TestRedisConfig.class)
class LikesServiceTest {

    @Autowired
    private LikesService likesService;
    @Autowired
    private PosterJpaRepository posterJpaRepository;
    @Autowired
    private ReplyJpaRepository replyJpaRepository;
    @Autowired
    private LikesJpaRepository likesJpaRepository;
    @MockBean
    private KafkaProducer kafkaProducer;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @AfterEach
    void afterEach() {
        likesJpaRepository.deleteAllInBatch();
        replyJpaRepository.deleteAllInBatch();
        posterJpaRepository.deleteAllInBatch();
    }

    @DisplayName("라이크 종류가 포스터인 경우")
    @Test
    public void createLikesWithTypePoster() throws Exception {
        //given
        Long userId = 1L;
        Poster poster = createPoster("포스터");
        posterJpaRepository.save(poster);
        Reply reply = createReply("리플", userId, poster);
        replyJpaRepository.save(reply);
        LikeType type = LikeType.POSTER;
        CreateLikesRequestDto requestDto = getCreateLikesRequestDtoBuilder(type, userId, null, poster.getId());

        //when
        Long likeId = likesService.createLikes(requestDto);

        //then
        Assertions.assertThat(likeId)
                .isNotNull();
    }

    @DisplayName("라이크 종류가 댓글인 경우")
    @Test
    public void createLikesWithTypeReply() throws Exception {
        //given
        Long userId = 1L;
        Poster poster = createPoster("포스터");
        posterJpaRepository.save(poster);
        Reply reply = createReply("리플", userId, poster);
        replyJpaRepository.save(reply);
        LikeType type = LikeType.REPLY;
        CreateLikesRequestDto requestDto = getCreateLikesRequestDtoBuilder(type, userId, reply.getId(), poster.getId());

        //when
        Long likeId = likesService.createLikes(requestDto);

        //then
        Assertions.assertThat(likeId)
                .isNotNull();
    }

    private static Poster createPoster(String title) {
        return Poster.builder().title(title).build();
    }

    private static Reply createReply(String contents,Long userId,Poster poster) {
        return Reply.builder()
                .userId(userId)
                .contents(contents)
                .poster(poster).build();
    }

    public static CreateLikesRequestDto getCreateLikesRequestDtoBuilder(LikeType type, Long userId, Long replyId, Long posterId) {
        return CreateLikesRequestDto.builder()
                .likeType(type)
                .userId(userId)
                .replyId(replyId)
                .posterId(posterId).build();
    }
}