package com.example.stockmsanewsfeed.service.newsFeedService;

import com.example.stockmsanewsfeed.client.activity.ActivityApi;
import com.example.stockmsanewsfeed.client.dto.request.user.GetMyFollowersRequestDto;
import com.example.stockmsanewsfeed.client.dto.request.user.GetUserRequestDto;
import com.example.stockmsanewsfeed.client.dto.response.user.FollowerDto;
import com.example.stockmsanewsfeed.client.dto.response.user.GetMyFollowersResponseDto;
import com.example.stockmsanewsfeed.client.dto.response.user.GetUserResponseDto;
import com.example.stockmsanewsfeed.client.dto.response.user.UserDto;
import com.example.stockmsanewsfeed.client.user.UserApi;
import com.example.stockmsanewsfeed.domain.newsFeed.ActivityType;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeed;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import com.example.stockmsanewsfeed.repository.newsFeedRepository.NewsFeedJpaRepository;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.NewsFeedDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.NewsFeedPageDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;


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
    private final Long userId = 1L;
    private final Long follower1Id = 2L;
    private final Long follower2Id = 3L;
    private final Long posterOwnerId = 4L;
    private final Long posterId = 1L;

    private static GetMyNewsFeedRequestDto getRequestDto(Long followerId, int page, int size) {
        return GetMyNewsFeedRequestDto.builder()
                .page(page)
                .size(size)
                .userId(followerId).build();
    }

    private static NewsFeed getNewsFeed(Long userId, NewsFeedType newsFeedType, Long activityUserId) {
        return NewsFeed.builder().userId(userId)
                .newsFeedType(newsFeedType)
                .activityUserId(activityUserId).build();
    }

    private static CreateNewsFeedRequestDto getCreateNewsFeedRequestDto(Long userId, ActivityType activityType, Long relatedUserId, Long relatedPosterId) {
        return CreateNewsFeedRequestDto.builder()
                .userId(userId)
                .activityType(activityType)
                .relatedUserId(relatedUserId)
                .relatedPosterId(relatedPosterId)
                .build();
    }

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
                .willReturn(ResponseDto.ofSuccess(
                                List.of(follower1, follower2)));

        given(userApi.getUserById(GetUserRequestDto.builder()
                .userId(1L)
                .build()))
                .willReturn(UserDto.builder()
                                .id(1L)
                                .name("user")
                                .build());
        given(userApi.getUserById(GetUserRequestDto.builder()
                .userId(2L)
                .build()))
                .willReturn(UserDto.builder()
                                .id(2L)
                                .name("follower1")
                                .build());
        given(userApi.getUserById(GetUserRequestDto.builder()
                .userId(3L)
                .build()))
                .willReturn(UserDto.builder()
                                .id(3L)
                                .name("follower2")
                                .build()
                );
    }

    @DisplayName("나의 뉴스피드 가져오기")
    @Test
    public void getMyNewsFeeds() throws Exception {
        //given
        Long userId = 1L;
        Long activityUserId = 2L;

        NewsFeed newsFeed1 = getNewsFeed(userId, FOLLOWING_POST, activityUserId);
        NewsFeed newsFeed2 = getNewsFeed(userId, FOLLOWING_LIKE, activityUserId);
        NewsFeed newsFeed3 = getNewsFeed(userId, FOLLOWING_REPLY, activityUserId);
        NewsFeed newsFeed4 = getNewsFeed(userId, FOLLOWING_FOLLOW, activityUserId);

        newsFeedJpaRepository.saveAll(List.of(newsFeed1, newsFeed2, newsFeed3, newsFeed4));

        int pageSize = 2;
        GetMyNewsFeedRequestDto requestDto0 = getRequestDto(userId, 0, pageSize);
        GetMyNewsFeedRequestDto requestDto1 = getRequestDto(userId, 1, pageSize);

        //when
        NewsFeedPageDto page0Response = newsFeedService.getMyNewsFeeds(requestDto0);
        NewsFeedPageDto page1Response = newsFeedService.getMyNewsFeeds(requestDto1);

        //then
        Assertions.assertThat(page0Response.getContents())
                .extracting("newsFeedType", "activityUserId")
                .containsExactly(
                        tuple(FOLLOWING_POST, activityUserId), tuple(FOLLOWING_LIKE, activityUserId)
                );
        Assertions.assertThat(page1Response.getContents())
                .extracting("newsFeedType", "activityUserId")
                .containsExactly(
                        tuple(FOLLOWING_REPLY, activityUserId), tuple(FOLLOWING_FOLLOW, activityUserId)
                );
    }

    @DisplayName("타입으로 나의 뉴스피드 가져오기")
    @Test
    public void getMyNewsFeedsByTypes() throws Exception {
        //given
        Long userId = 1L;
        Long activityUserId = 2L;

        NewsFeed newsFeed1 = getNewsFeed(userId, FOLLOWING_POST, activityUserId);
        NewsFeed newsFeed2 = getNewsFeed(userId, FOLLOWING_LIKE, activityUserId);
        NewsFeed newsFeed3 = getNewsFeed(userId, FOLLOWING_REPLY, activityUserId);
        NewsFeed newsFeed4 = getNewsFeed(userId, FOLLOWING_FOLLOW, activityUserId);

        newsFeedJpaRepository.saveAll(List.of(newsFeed1, newsFeed2, newsFeed3, newsFeed4));

        GetMyNewsFeedByTypesRequestDto requestDto0
                = GetMyNewsFeedByTypesRequestDto.builder()
                .userId(userId)
                .size(2)
                .page(0)
                .newsFeedTypeList(List.of(FOLLOWING_POST,FOLLOWING_LIKE))
                .build();

        //when
        NewsFeedPageDto newsFeedPageDto = newsFeedService.getMyNewsFeedsByType(requestDto0);

        //then
        Assertions.assertThat(newsFeedPageDto.getContents())
                .extracting("newsFeedType", "activityUserId")
                .containsExactly(
                        tuple(FOLLOWING_POST, activityUserId), tuple(FOLLOWING_LIKE, activityUserId)
                );
    }

    @DisplayName("게시글을 작성시 유저의 팔로워들의 뉴스피드 생성")
    @Test
    public void createNewsFeedWithPOST() throws Exception {
        //given
        ActivityType activityType = ActivityType.POST;
        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(userId, activityType, null, posterId);

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
        all.forEach(a -> System.out.println(a.getUserId()));

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

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(userId, activityType, posterOwnerId, null);
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

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(userId, activityType, posterOwnerId, posterId);

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
}