package com.example.stockmsauser.service.followService;


import com.example.stockmsauser.client.api.newsFeed.NewsFeedApi;
import com.example.stockmsauser.client.api.newsFeed.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsauser.common.error.exception.DatabaseErrorException;
import com.example.stockmsauser.common.error.exception.DuplicateFollowException;
import com.example.stockmsauser.common.error.exception.InternalServerErrorException;
import com.example.stockmsauser.domain.follow.Follow;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.kafka.KafkaProducer;
import com.example.stockmsauser.repository.followRepository.FollowJpaRepository;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.FollowerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowJpaRepository followJpaRepository;
    private final UserJpaRepository userJpaRepository;

    private final KafkaProducer kafkaProducer;

    @Override
    public boolean follow(StartFollowRequestDto dto) {
        Long followingId = dto.getFollowingId();
        Long followerId = dto.getFollowerId();

        User follower = userJpaRepository.findById(followerId)
                .orElseThrow(()->new DatabaseErrorException("잘못된 요청"));
        User following = userJpaRepository.findById(followingId)
                .orElseThrow(()->new DatabaseErrorException("잘못된 요청"));

        boolean isDuplicated = followJpaRepository.existsByFollowingIdAndFollowerId(followingId, followerId);
        if(isDuplicated) throw new DuplicateFollowException("중복된 팔로우");

        Follow follow = Follow.builder().follower(follower).following(following).build();
        followJpaRepository.save(follow);

        //뉴스피드 생성 서비스 호출 !
        CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                .activityType("FOLLOW")
                .userId(followerId)
                .relatedUserId(followingId)
                .build();
        try {
            kafkaProducer.createNewsFeed(createNewsFeedRequestDto);
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
            throw new InternalServerErrorException("MS간 통신 에러");
        }
        return true;
    }

    @Override
    public List<FollowerDto> getMyFollower(GetMyFollowersRequestDto dto) {
        List<Follow> followList = followJpaRepository.findByFollowingId(dto.getUserId());
        List<FollowerDto> follwerList = followList.stream().map(follow -> FollowerDto.builder().followerName(follow.getFollower().getName())
                .followerId(follow.getFollower().getId()).build()).collect(Collectors.toList());
        return follwerList;
    }
}
