package com.example.stockmsauser.service.followService;


import com.example.stockmsauser.domain.follow.Follow;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.client.api.newsFeed.NewsFeedApi;
import com.example.stockmsauser.client.api.newsFeed.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsauser.repository.followRepository.FollowJpaRepository;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.follow.GetMyFollowersRequestDto;
import com.example.stockmsauser.web.dto.request.follow.StartFollowRequestDto;
import com.example.stockmsauser.web.dto.response.follow.FollowerDto;
import com.example.stockmsauser.web.dto.response.follow.GetMyFollowersResponseDto;
import com.example.stockmsauser.web.dto.response.follow.StartFollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowJpaRepository followJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final NewsFeedApi newsFeedApi;


    @Override
    public ResponseEntity<? super StartFollowResponseDto> follow(StartFollowRequestDto dto) {
        Long followingId = dto.getFollowingId();
        Long followerId = dto.getFollowerId();
        try {
            User follower = userJpaRepository.findById(followerId).get();
            User following = userJpaRepository.findById(followingId).get();

            Follow follow = Follow.builder().follower(follower).following(following).build();
            followJpaRepository.save(follow);

            /**
             * 만들어야 함!!
             */
            //뉴스피드 생성 서비스 호출 !
            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .build();
            newsFeedApi.createNewsFeed(createNewsFeedRequestDto);

        } catch (Exception exception) {
            exception.printStackTrace();
            StartFollowResponseDto.databaseError();
        }

        return StartFollowResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetMyFollowersResponseDto> getMyFollower(GetMyFollowersRequestDto dto) {
        List<Follow> followList = followJpaRepository.findByFollowingId(dto.getUserId());
        List<FollowerDto> follwerList = followList.stream().map(follow -> FollowerDto.builder().followerName(follow.getFollower().getName())
                .followerId(follow.getFollower().getId()).build()).collect(Collectors.toList());

        return GetMyFollowersResponseDto.success(follwerList);
    }


}
