package com.example.stockmsaactivity.repository.posterRepository;

import com.example.stockmsaactivity.domain.poster.Poster;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PosterJpaRepositoryTest {

    @Autowired
    private PosterJpaRepository posterJpaRepository;

    @AfterEach
    void afterEach() {
        posterJpaRepository.deleteAllInBatch();
    }

    @DisplayName("like 카운트 업데이트")
    @Test
    public void addLikeCountFromRedis() throws Exception {
        //given
        Poster poster = Poster.builder()
                .title("타이틀")
                .contents("컨텐츠").build();
        Poster save = posterJpaRepository.save(poster);
        int likeCount = 10;
        Long posterId = save.getId();
        //when
        posterJpaRepository.addLikeCountFromRedis(posterId,likeCount);
        Poster posterById = posterJpaRepository.findById(posterId).get();
        int NewlikesCount = posterById.getLikeCount();

        //then
        Assertions.assertThat(NewlikesCount).isEqualTo(likeCount);
    }

    @DisplayName("유저 아이디로 찾기")
    @Test
    public void findAllByUserId() throws Exception {
        //given
        Poster poster1 = Poster.builder().userId(1L)
                .title("포스터1").build();
        Poster poster2 = Poster.builder().userId(1L)
                .title("포스터2").build();
        Poster poster3 = Poster.builder().userId(1L)
                .title("포스터3").build();

        posterJpaRepository.saveAll(List.of(poster1, poster2, poster3));
        //when
        List<Poster> byUserId = posterJpaRepository.findAllByUserId(poster1.getUserId());

        //then
        Assertions.assertThat(byUserId).extracting("title")
                .containsExactly("포스터1", "포스터2", "포스터3");
    }

    @DisplayName("포스터 아이디 리스트로 찾기")
    @Test
    public void findAllByIdIn() throws Exception {
        //given
        Poster poster1 = Poster.builder()
                .title("포스터1").build();
        Poster poster2 = Poster.builder()
                .title("포스터2").build();
        Poster poster3 = Poster.builder()
                .title("포스터3").build();
        posterJpaRepository.saveAll(List.of(poster1, poster2, poster3));
        //when
        List<Poster> allByIdIn = posterJpaRepository.findAllByIdIn(List.of(poster1.getId(), poster2.getId()));

        //then
        Assertions.assertThat(allByIdIn).extracting("title")
                .containsExactly("포스터1", "포스터2");
    }

}