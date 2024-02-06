package com.example.stockmsauser.repository.followRepository;

import com.example.stockmsauser.domain.follow.Follow;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.netflix.discovery.EurekaClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FollowJpaRepositoryTest {
    @MockBean
    private EurekaClient eurekaClient;
    @Autowired
    private FollowJpaRepository followJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @DisplayName("팔로잉 아이디를 통하여 팔로우 정보 찾기")
    @Test
    public void findByFollowingId() throws Exception {
        //given
        User user1 = User.builder().name("user1")
                .email("email1@naver.com")
                .password("1234")
                .introduction("안녕")
                .imgPath("1234")
                .build();
        userJpaRepository.save(user1);
        User user2 = User.builder().name("user2").build();
        userJpaRepository.save(user2);
        User user3 = User.builder().name("user3").build();
        userJpaRepository.save(user3);


        Follow follow1 = Follow.builder().following(user2).follower(user1).build();
        Follow follow2 = Follow.builder().following(user3).follower(user2).build();
        Follow follow3 = Follow.builder().following(user3).follower(user1).build();
        followJpaRepository.saveAll(asList(follow1, follow2, follow3));

        //when
        Long user3Id = user3.getId();
        List<Follow> user3FollowList = followJpaRepository.findByFollowingId(user3Id);
        Long user2Id = user2.getId();
        List<Follow> user2FollowList = followJpaRepository.findByFollowingId(user2Id);
        //then
        Assertions.assertThat(user3FollowList).containsExactlyInAnyOrder(follow2, follow3);
        Assertions.assertThat(user2FollowList).containsExactlyInAnyOrder(follow1);
    }
}