package com.example.stockmsanewsfeed.service.newsFeedService;

import com.example.stockmsanewsfeed.common.ResponseCode;
import com.example.stockmsanewsfeed.common.ResponseMessage;
import com.example.stockmsanewsfeed.domain.newsFeed.ActivityType;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeed;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import com.example.stockmsanewsfeed.repository.newsFeedRepository.NewsFeedJpaRepository;
import com.example.stockmsanewsfeed.web.api.activity.ActivityApi;
import com.example.stockmsanewsfeed.web.api.dto.request.activity.GetPosterRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.request.user.GetMyFollowersRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.request.user.GetUserRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.response.PosterDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.FollowerDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.GetMyFollowersResponseDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.GetUserResponseDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.UserDto;
import com.example.stockmsanewsfeed.web.api.user.UserApi;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.BDDMockito.*;


@SpringBootTest
@Transactional
class NewsFeedServiceTest {

    @Autowired
    NewsFeedService newsFeedService;
    @Autowired
    NewsFeedJpaRepository newsFeedJpaRepository;
    @MockBean
    private UserApi userApi;
    @MockBean
    private ActivityApi activityApi;
    private Long userId = 1L;
    private Long follower1Id = 2L;
    private Long follower2Id = 3L;
    private Long posterOwnerId = 4L;
    private Long posterId = 1L;

    @BeforeEach
    void setUp() {
        FollowerDto follower1 = FollowerDto.builder()
                .followerId(2L)
                .followerName("follower1")
                .build();

        FollowerDto follower2 = FollowerDto.builder()
                .followerId(3L)
                .followerName("follower2")
                .build();

        given(userApi.getMyFollower(any(GetMyFollowersRequestDto.class)))
                .willReturn(
                        GetMyFollowersResponseDto.builder()
                                .followerList(List.of(follower1,follower2))
                        .build());

        given(userApi.getUserById(GetUserRequestDto.builder()
                .userId(1L)
                .build()))
                .willReturn(GetUserResponseDto.builder()
                        .userDto(UserDto.builder()
                                .id(1L)
                                .name("user")
                                .build())
                                .build()
                        );
        given(userApi.getUserById(GetUserRequestDto.builder()
                .userId(2L)
                .build()))
                .willReturn(GetUserResponseDto.builder()
                        .userDto(UserDto.builder()
                                .id(2L)
                                .name("follower1")
                                .build())
                        .build()
                );
        given(userApi.getUserById(GetUserRequestDto.builder()
                .userId(3L)
                .build()))
                .willReturn(GetUserResponseDto.builder()
                        .userDto(UserDto.builder()
                                .id(3L)
                                .name("follower2")
                                .build())
                        .build()
                );
    }

    @DisplayName("나의 뉴스피드 가져오기")
    @Test
    public void getMyNewsFeeds() throws Exception {
        //given
        Long userId = 1L;
        Long activityUserId = 2L;

        NewsFeed newsFeed1 = getNewsFeed(userId, FOLLOWING_POST ,activityUserId);
        NewsFeed newsFeed2 = getNewsFeed(userId, FOLLOWING_LIKE ,activityUserId);
        NewsFeed newsFeed3 = getNewsFeed(userId, FOLLOWING_REPLY ,activityUserId);
        NewsFeed newsFeed4 = getNewsFeed(userId, FOLLOWING_FOLLOW ,activityUserId);

        newsFeedJpaRepository.saveAll(List.of(newsFeed1, newsFeed2, newsFeed3, newsFeed4));

        int pageSize = 2;
        GetMyNewsFeedRequestDto requestDto0 = getRequestDto(userId, 0, pageSize);
        GetMyNewsFeedRequestDto requestDto1 = getRequestDto(userId, 1, pageSize);

        //when
        GetMyNewsFeedResponseDto page0Response = (GetMyNewsFeedResponseDto) newsFeedService.getMyNewsFeeds(requestDto0).getBody();
        GetMyNewsFeedResponseDto page1Response = (GetMyNewsFeedResponseDto) newsFeedService.getMyNewsFeeds(requestDto1).getBody();

        //then
        Assertions.assertThat(page0Response)
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        Assertions.assertThat(page1Response)
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        Assertions.assertThat(page0Response.getNewsFeedPage().getContent())
                .extracting("newsFeedType", "activityUserId")
                .containsExactly(
                        tuple(FOLLOWING_POST, activityUserId), tuple(FOLLOWING_LIKE, activityUserId)
                );
        Assertions.assertThat(page1Response.getNewsFeedPage().getContent())
                .extracting("newsFeedType", "activityUserId")
                .containsExactly(
                        tuple(FOLLOWING_REPLY, activityUserId), tuple(FOLLOWING_FOLLOW, activityUserId)
                );
    }

    private static GetMyNewsFeedRequestDto getRequestDto(Long followerId, int page, int size) {
        return GetMyNewsFeedRequestDto.builder()
                .page(page)
                .size(size)
                .userId(followerId).build();
    }

    private static NewsFeed getNewsFeed(Long userId, NewsFeedType newsFeedType , Long activityUserId) {
        return NewsFeed.builder().userId(userId)
                .newsFeedType(newsFeedType)
                .activityUserId(activityUserId).build();
    }

    @DisplayName("게시글을 작성시 유저의 팔로워들의 뉴스피드 생성")
    @Test
    public void createNewsFeedWithPOST() throws Exception {
        //given
        ActivityType activityType = ActivityType.POST;


        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(userId, activityType, null,posterId);
        //when
        newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        List<NewsFeed> follower1NewsFeed = newsFeedJpaRepository.findAllByUserId(follower1Id);
        List<NewsFeed> follower2NewsFeed = newsFeedJpaRepository.findAllByUserId(follower2Id);
        List<NewsFeed> all = newsFeedJpaRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);
        assertThat(follower1NewsFeed)
                .extracting("newsFeedType", "activityUserId", "relatedPosterId")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_POST, userId, posterId));
        assertThat(follower2NewsFeed)
                .extracting("newsFeedType", "activityUserId", "relatedPosterId")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_POST, userId, posterId));
    }

    @DisplayName("댓글 작성시 팔로워들과 포스터 주인의 뉴스피드 생성")
    @Test
    public void createNewsFeedWithREPLY() throws Exception {
        //given
        ActivityType activityType = ActivityType.REPLY;

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(userId, activityType, posterOwnerId, posterId);
        //when
        newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        List<NewsFeed> follower1NewsFeed = newsFeedJpaRepository.findAllByUserId(follower1Id);
        List<NewsFeed> follower2NewsFeed = newsFeedJpaRepository.findAllByUserId(follower2Id);
        List<NewsFeed> posterOwnerNewsFeed = newsFeedJpaRepository.findAllByUserId(posterOwnerId);
        List<NewsFeed> all = newsFeedJpaRepository.findAll();

        System.out.println("프린트");
        all.forEach(a-> System.out.println(a.getUserId()));

        //then
        assertThat(all.size()).isEqualTo(3);
        assertThat(follower1NewsFeed)
                .extracting("newsFeedType", "activityUserId", "relatedPosterId")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_REPLY, userId, posterId));
        assertThat(follower2NewsFeed)
                .extracting("newsFeedType", "activityUserId", "relatedPosterId")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_REPLY, userId, posterId));
        assertThat(posterOwnerNewsFeed)
                .extracting("newsFeedType", "activityUserId", "relatedPosterId")
                .containsExactlyInAnyOrder(tuple(MY_REPLY, userId, posterId));
    }

    @DisplayName("유저가 팔로우시 그 팔로잉과 유저의 팔로워들에게 뉴스피드 생성 // 유저가 포스터 주인을 팔로우 했다고 가정")
    @Test
    public void createNewsFeedWithFOLLOW() throws Exception {
        //given
        ActivityType activityType = ActivityType.FOLLOW;

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(userId, activityType, posterOwnerId , null);
        //when
        newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        List<NewsFeed> follower1NewsFeed = newsFeedJpaRepository.findAllByUserId(follower1Id);
        List<NewsFeed> follower2NewsFeed = newsFeedJpaRepository.findAllByUserId(follower2Id);
        List<NewsFeed> posterOwnerNewsFeed = newsFeedJpaRepository.findAllByUserId(posterOwnerId);
        List<NewsFeed> all = newsFeedJpaRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(3);
        assertThat(follower1NewsFeed)
                .extracting("newsFeedType", "activityUserId")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_FOLLOW, userId));
        assertThat(follower2NewsFeed)
                .extracting("newsFeedType", "activityUserId")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_FOLLOW, userId));
        assertThat(posterOwnerNewsFeed)
                .extracting("newsFeedType", "activityUserId")
                .containsExactlyInAnyOrder(tuple(MY_FOLLOW, userId));
    }

    @DisplayName("유저가 좋아요를 누를 시 관련 게시물의 소유자와 팔로워들에게 뉴스피드 생성")
    @Test
    public void createNewsFeedWithLIKE() throws Exception {
        //given
        ActivityType activityType = ActivityType.LIKE;

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(userId, activityType, posterOwnerId , posterId);

        //when
        newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        List<NewsFeed> follower1NewsFeed = newsFeedJpaRepository.findAllByUserId(follower1Id);
        List<NewsFeed> follower2NewsFeed = newsFeedJpaRepository.findAllByUserId(follower2Id);
        List<NewsFeed> posterOwnerNewsFeed = newsFeedJpaRepository.findAllByUserId(posterOwnerId);
        List<NewsFeed> all = newsFeedJpaRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(3);
        assertThat(follower1NewsFeed)
                .extracting("newsFeedType", "activityUserId")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_LIKE, userId));
        assertThat(follower2NewsFeed)
                .extracting("newsFeedType", "activityUserId")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_LIKE, userId));
        assertThat(posterOwnerNewsFeed)
                .extracting("newsFeedType", "activityUserId")
                .containsExactlyInAnyOrder(tuple(MY_LIKE, userId));
    }


    private static CreateNewsFeedRequestDto getCreateNewsFeedRequestDto(Long userId, ActivityType activityType , Long relatedUserId, Long relatedPosterId) {
        return CreateNewsFeedRequestDto.builder()
                .userId(userId)
                .activityType(activityType)
                .relatedUserId(relatedUserId)
                .relatedPosterId(relatedPosterId)
                .build();
    }
}