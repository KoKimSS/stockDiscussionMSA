package com.example.stockmsaactivity.repository.replyRepository;

import com.example.stockmsaactivity.domain.poster.Poster;
import com.example.stockmsaactivity.domain.reply.Reply;
import com.example.stockmsaactivity.repository.posterRepository.PosterJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyJpaRepositoryTest {

    @Autowired
    private ReplyJpaRepository replyJpaRepository;

    @Autowired
    private PosterJpaRepository posterJpaRepository;
    @AfterEach
    void afterEach() {
        replyJpaRepository.deleteAllInBatch();
    }

    @DisplayName("라이크 카운트 업데이트")
    @Test
    public void addLikeCountFromRedis() throws Exception {
        //given
        Reply reply = Reply.builder().contents("라이크")
                .build();
        Reply save = replyJpaRepository.save(reply);
        int likeCount = 3;
        //when
        replyJpaRepository.addLikeCountFromRedis(save.getId(), likeCount);
        Reply replyById = replyJpaRepository.findById(save.getId()).get();
        int updatedLikeCount = replyById.getLikeCount();
        //then
        Assertions.assertThat(updatedLikeCount).isEqualTo(likeCount);
    }


    @DisplayName("포스터 아이디로 찾기")
    @Test
    public void findAllByPosterId() throws Exception {
        //given
        Poster poster = Poster.builder().title("포스터").build();
        Poster save = posterJpaRepository.save(poster);
        Long posterId = save.getId();
        Reply reply1 = Reply.builder()
                .contents("댓글1")
                .poster(save).build();
        Reply reply2 = Reply.builder()
                .contents("댓글2")
                .poster(save).build();
        Reply reply3 = Reply.builder()
                .contents("댓글3")
                .poster(save).build();
        replyJpaRepository.saveAll(List.of(reply1, reply2, reply3));

        //when
        List<Reply> allByPosterId = replyJpaRepository.findAllByPosterId(posterId);

        //then
        Assertions.assertThat(allByPosterId).extracting("contents")
                .containsExactly("댓글1", "댓글2", "댓글3");
    }
}