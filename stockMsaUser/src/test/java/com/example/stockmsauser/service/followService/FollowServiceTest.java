package com.example.stockmsauser.service.followService;

import com.example.stockmsauser.common.ResponseCode;
import com.example.stockmsauser.common.ResponseMessage;
import com.example.stockmsauser.domain.follow.Follow;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.followRepository.FollowJpaRepository;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.StartFollowResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FollowServiceTest {

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowJpaRepository followJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @DisplayName("팔로우시 팔로우 안에 팔로워 팔로잉 유저가 모두 있어야 한다")
    @Test
    public void follow() throws Exception {
        //given
        User follower = createUser("follower");
        User following = createUser("following");
        userJpaRepository.saveAll(List.of(follower, following));
        StartFollowRequestDto requestDto = createStartFollowRequestDto(follower.getId(), following.getId());

        //when
        ResponseEntity<? super StartFollowResponseDto> response = followService.follow(requestDto);

        //then
        List<Follow> byFollowingId = followJpaRepository.findByFollowingId(following.getId());
        List<Follow> byFollowerId = followJpaRepository.findByFollowerId(follower.getId());

        Assertions.assertThat(byFollowingId)
                .extracting("follower")
                .containsExactly(follower);
        Assertions.assertThat(byFollowerId)
                .extracting("following")
                .containsExactly(following);
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private static StartFollowRequestDto createStartFollowRequestDto(Long followerId,Long followingId) {
        return StartFollowRequestDto.builder().followerId(followerId).followingId(followingId).build();
    }

    private static User createUser(String name) {
        return User.builder().name(name).build();
    }
}