package com.example.stockmsanewsfeed.repository.newsFeedRepository;

import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeed;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
//@Transactional
class NewsFeedJpaRepositoryTest {

    @Autowired
    NewsFeedJpaRepository newsFeedJpaRepository;

    @AfterEach
    void afterEach() {
        newsFeedJpaRepository.deleteAllInBatch();
    }
    @DisplayName("유저 아이디로 뉴스피드 페이지를 찾는다.")
    @Test
    public void findByUserId() throws Exception {
        //given
        Long userId = 1L;
        Long activityUser1 = 2L;
        Long activityUser2 = 3L;
        Long posterId = 1L;

        NewsFeed newsFeed_MY_REPLY = getNewsFeed(userId, activityUser1, posterId,NewsFeedType.MY_REPLY,null);
        NewsFeed newsFeed_MY_LIKE = getNewsFeed(userId, activityUser1, posterId,NewsFeedType.MY_LIKE,null);
        NewsFeed newsFeed_MY_FOLLOW = getNewsFeed(userId, activityUser1, null, NewsFeedType.MY_FOLLOW,null);
        NewsFeed newsFeed_FOLLOWING_REPLY = getNewsFeed(userId, activityUser1, posterId,NewsFeedType.FOLLOWING_REPLY,activityUser2);

        int size = 2;
        Pageable pageable0 = PageRequest.of(0, size);
        Pageable pageable1 = PageRequest.of(1, size);

        //when
        newsFeedJpaRepository.saveAll(asList(newsFeed_MY_REPLY, newsFeed_MY_LIKE, newsFeed_MY_FOLLOW, newsFeed_FOLLOWING_REPLY));
        Page<NewsFeed> page1 = newsFeedJpaRepository.findByUserId(userId, pageable0);
        Page<NewsFeed> page2 = newsFeedJpaRepository.findByUserId(userId, pageable1);

        //then
        Assertions.assertThat(page1.getTotalElements()).isEqualTo(4);
        Assertions.assertThat(page1.getContent()).containsExactly(newsFeed_MY_REPLY, newsFeed_MY_LIKE);
        Assertions.assertThat(page2.getTotalElements()).isEqualTo(4);
        Assertions.assertThat(page2.getContent()).containsExactly(newsFeed_MY_FOLLOW, newsFeed_FOLLOWING_REPLY);
    }

    @DisplayName("유저 아이디와 뉴스피드 타입 뉴스피드 페이지를 찾는다.")
    @Test
    public void findByUserIdAndTypeIn() throws Exception {
        //given
        Long userId = 1L;
        Long activityUser1 = 2L;
        Long activityUser2 = 3L;
        Long posterId = 1L;

        NewsFeed newsFeed_MY_REPLY = getNewsFeed(userId, activityUser1, posterId,NewsFeedType.MY_REPLY,null);
        NewsFeed newsFeed_MY_LIKE = getNewsFeed(userId, activityUser1, posterId,NewsFeedType.MY_LIKE,null);
        NewsFeed newsFeed_MY_FOLLOW = getNewsFeed(userId, activityUser1, null, NewsFeedType.MY_FOLLOW,null);
        NewsFeed newsFeed_FOLLOWING_REPLY = getNewsFeed(userId, activityUser1, posterId,NewsFeedType.FOLLOWING_REPLY,activityUser2);

        int size = 2;
        Pageable pageable0 = PageRequest.of(0, size);

        //when
        newsFeedJpaRepository.saveAll(asList(newsFeed_MY_REPLY, newsFeed_MY_LIKE, newsFeed_MY_FOLLOW, newsFeed_FOLLOWING_REPLY));
        Page<NewsFeed> page1 = newsFeedJpaRepository.findByUserIdAndNewsFeedTypeIn(userId, List.of(NewsFeedType.MY_LIKE) ,pageable0);

        //then
        Assertions.assertThat(page1.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(page1.getContent()).containsExactly(newsFeed_MY_LIKE);
    }

    private static NewsFeed getNewsFeed(Long userId, Long activityUserId, Long posterId, NewsFeedType newsFeedType, Long relatedUserId) {
        return NewsFeed.builder().userId(userId)
                .activityUserId(activityUserId)
                .newsFeedType(newsFeedType)
                .relatedPosterId(posterId)
                .relatedUserId(relatedUserId)
                .build();
    }

}