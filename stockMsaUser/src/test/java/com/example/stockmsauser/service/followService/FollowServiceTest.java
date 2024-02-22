package com.example.stockmsauser.service.followService;

import com.example.stockmsauser.client.api.newsFeed.NewsFeedApi;
import com.example.stockmsauser.client.api.newsFeed.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsauser.common.error.exception.DatabaseErrorException;
import com.example.stockmsauser.common.error.exception.DuplicateFollowException;
import com.example.stockmsauser.domain.follow.Follow;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.followRepository.FollowJpaRepository;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.auth.EmailCheckRequestDto;
import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.FollowerDto;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class FollowServiceTest {

    @Autowired
    private FollowService followService;
    @Autowired
    private FollowJpaRepository followJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @MockBean
    private NewsFeedApi newsFeedApi;

    @DisplayName("팔로우시 팔로우 안에 팔로워 팔로잉 유저가 모두 있어야 한다")
    @Test
    public void follow() throws Exception {
        //given
        User follower = createUser("follower");
        User following = createUser("following");
        userJpaRepository.saveAll(List.of(follower, following));
        StartFollowRequestDto requestDto = createStartFollowRequestDto(follower.getId(), following.getId());
        BDDMockito.doReturn(null).when(newsFeedApi)
                .createNewsFeed(any(CreateNewsFeedRequestDto.class));

        //when
        boolean isSuccess = followService.follow(requestDto);

        //then
        List<Follow> byFollowingId = followJpaRepository.findByFollowingId(following.getId());
        List<Follow> byFollowerId = followJpaRepository.findByFollowerId(follower.getId());

        assertThat(byFollowingId)
                .extracting("follower")
                .containsExactly(follower);
        assertThat(byFollowerId)
                .extracting("following")
                .containsExactly(following);
        assertThat(isSuccess).isTrue();
    }

    @DisplayName("팔로우시 올바른 팔로잉 id 팔로워 id 값을 보내주지 않으면 error 발생")
    @Test
    public void followWithNotValidId() throws Exception {
        //given
        User follower = createUser("follower");
        User following = createUser("following");
        userJpaRepository.saveAll(List.of(follower, following));
        StartFollowRequestDto requestDto = createStartFollowRequestDto(follower.getId()+11, following.getId());
        BDDMockito.doReturn(null).when(newsFeedApi)
                .createNewsFeed(any(CreateNewsFeedRequestDto.class));

        //when, then
        assertThrows(DatabaseErrorException.class,()->followService.follow(requestDto));
    }

    @DisplayName("팔로우시 중복된 팔로우면 error 발생")
    @Test
    public void followWithDuplicateFollow() throws Exception {
        //given
        User follower = createUser("follower");
        User following = createUser("following");
        userJpaRepository.saveAll(List.of(follower, following));
        BDDMockito.doReturn(null).when(newsFeedApi)
                .createNewsFeed(any(CreateNewsFeedRequestDto.class));
        StartFollowRequestDto requestDto = createStartFollowRequestDto(follower.getId(), following.getId());

        //when, then
        assertThat(followService.follow(requestDto)).isTrue();
        assertThrows(DuplicateFollowException.class,()->followService.follow(requestDto));
    }

    @DisplayName("나의 팔로워 목록 가져오기")
    @Test
    public void getMyFollower() throws Exception {
        //given
        User follower1 = createUser("follower1");
        User follower2 = createUser("follower2");
        User following = createUser("following");
        userJpaRepository.saveAll(List.of(follower1, follower2, following));
        Follow follow1 = Follow.builder().follower(follower1).following(following).build();
        Follow follow2 = Follow.builder().follower(follower2).following(following).build();
        followJpaRepository.saveAll(List.of(follow1,follow2));
        GetMyFollowersRequestDto requestDto = GetMyFollowersRequestDto.builder().userId(following.getId()).build();

        //when
        List<FollowerDto> myFollower = followService.getMyFollower(requestDto);

        //then
        Assertions.assertThat(myFollower)
                .extracting("followerName","followerId")
                .containsExactly(
                        tuple("follower1",follower1.getId()),
                        tuple("follower2",follower2.getId())
                );
    }

    private static StartFollowRequestDto createStartFollowRequestDto(Long followerId,Long followingId) {
        return StartFollowRequestDto.builder().followerId(followerId).followingId(followingId).build();
    }

    private static User createUser(String name) {
        return User.builder().name(name).build();
    }
}