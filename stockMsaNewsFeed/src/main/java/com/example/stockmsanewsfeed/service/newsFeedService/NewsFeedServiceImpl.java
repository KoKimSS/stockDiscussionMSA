package com.example.stockmsanewsfeed.service.newsFeedService;


import com.example.stockmsanewsfeed.client.dto.request.user.GetMyFollowersRequestDto;
import com.example.stockmsanewsfeed.client.dto.response.user.FollowerDto;
import com.example.stockmsanewsfeed.client.user.UserApi;
import com.example.stockmsanewsfeed.common.error.exception.DatabaseErrorException;
import com.example.stockmsanewsfeed.common.error.exception.InternalServerErrorException;
import com.example.stockmsanewsfeed.common.error.exception.ValidationFailException;
import com.example.stockmsanewsfeed.domain.newsFeed.ActivityType;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeed;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import com.example.stockmsanewsfeed.repository.newsFeedRepository.NewsFeedJpaRepository;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedByTypesRequestDto;
import com.example.stockmsanewsfeed.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.CreateNewsFeedResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedByTypeResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.NewsFeedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType.*;


@Service
@RequiredArgsConstructor
@Transactional
public class NewsFeedServiceImpl implements NewsFeedService {

    private final NewsFeedMapper newsFeedMapper;
    private final NewsFeedJpaRepository newsFeedJpaRepository;
    private final UserApi userApi;

    private static boolean isValidRequestDto(ActivityType activityType, Long relatedUserId, Long posterId) {
        return (activityType != ActivityType.POST && relatedUserId == null)
                || (activityType != ActivityType.FOLLOW && posterId == null);
    }

    private static List<NewsFeed> createFollowersNewsFeedList(Long userId, NewsFeedType newsFeedType, List<FollowerDto> followList, Long posterId, Long relatedUserId) {
        return followList.stream()
                .map(followerDto -> {
                    return NewsFeed.builder()
                            .newsFeedType(newsFeedType)
                            .userId(followerDto.getFollowerId())
                            .activityUserId(userId)
                            .relatedPosterId(posterId)
                            .relatedUserId(relatedUserId)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeeds(GetMyNewsFeedRequestDto dto) {
        Long userId = dto.getUserId();
        int page = dto.getPage();
        int size = dto.getSize();
        Pageable pageable = PageRequest.of(page, size);

        Page<NewsFeed> newsFeedPage = newsFeedJpaRepository.findByUserId(userId, pageable);
        if (newsFeedPage == null) throw new DatabaseErrorException("db에러");

        Page<NewsFeedDto> newsFeedDtoPage = newsFeedPage.map(newsFeedMapper::toGetMyNewsFeedDto);
        return GetMyNewsFeedResponseDto.success(newsFeedDtoPage);
    }

    @Override
    public ResponseEntity<? super GetMyNewsFeedByTypeResponseDto> getMyNewsFeedsByType(GetMyNewsFeedByTypesRequestDto dto) {
        Long userId = dto.getUserId();
        int page = dto.getPage();
        int size = dto.getSize();
        List<NewsFeedType> newsFeedTypeList = dto.getNewsFeedTypeList();
        Pageable pageable = PageRequest.of(page, size);

        Page<NewsFeed> newsFeedPage = newsFeedJpaRepository.findByUserIdAndNewsFeedTypeIn(userId, newsFeedTypeList, pageable);
        if (newsFeedPage == null) throw new DatabaseErrorException("db에러");

        Page<NewsFeedDto> newsFeedDtoPage = newsFeedPage.map(newsFeedMapper::toGetMyNewsFeedDto);
        return GetMyNewsFeedByTypeResponseDto.success(newsFeedDtoPage);
    }

    @Override
    public ResponseEntity<? super CreateNewsFeedResponseDto> createNewsFeed(
            CreateNewsFeedRequestDto dto) {
        //나를 팔로우 하는 사람들의 뉴스피드 리스트 생성
        ActivityType activityType = dto.getActivityType();
        NewsFeedType followersNewsFeedType = followersTypeBy(activityType);
        Long userId = dto.getUserId();
        Long relatedUserId = dto.getRelatedUserId();
        Long relatedPosterId = dto.getRelatedPosterId();

        if (isValidRequestDto(activityType, relatedUserId, relatedPosterId)) {
            throw new ValidationFailException("newsFeed request valid fail");
        }

        GetMyFollowersRequestDto getMyFollowersRequestDto = GetMyFollowersRequestDto.builder().userId(userId).build();
        List<FollowerDto> followerList;
        try {
            followerList = userApi.getMyFollower(getMyFollowersRequestDto).getFollowerList();
        } catch (Exception e) {
            throw new InternalServerErrorException("internal api error");
        }

        followerList.forEach(followerDto -> System.out.println(followerDto.getFollowerId() + " " + followerDto.getFollowerName()));
        List<NewsFeed> newsFeedList = createFollowersNewsFeedList(userId, followersNewsFeedType, followerList, relatedPosterId, relatedUserId);

        //내가 한 활동의 관련된 사람 뉴스피드 추가 ( POST 인 경우 관련유저 없음)
        if (activityType != ActivityType.POST) {
            NewsFeed newsFeed = NewsFeed.builder()
                    .newsFeedType(ownerTypeBy(activityType))
                    .userId(relatedUserId)
                    .activityUserId(userId)
                    .relatedPosterId(relatedPosterId)
                    .build();
            newsFeedList.add(newsFeed);
        }

        try {
            newsFeedJpaRepository.saveAll(newsFeedList);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new DatabaseErrorException("뉴스피드 생성 db 에러");
        }
        return CreateNewsFeedResponseDto.success();
    }

    private NewsFeedType followersTypeBy(ActivityType activityType) {
        if (activityType == ActivityType.POST) {
            return FOLLOWING_POST;
        }
        if (activityType == ActivityType.FOLLOW) {
            return FOLLOWING_FOLLOW;
        }
        if (activityType == ActivityType.REPLY) {
            return FOLLOWING_REPLY;
        }
        if (activityType == ActivityType.LIKE) {
            return FOLLOWING_LIKE;
        }
        throw new IllegalArgumentException("Activity 타입이 NewsFeedType 과 매칭이 안됩니다");
    }

    private NewsFeedType ownerTypeBy(ActivityType activityType) {
        if (activityType == ActivityType.FOLLOW) {
            return MY_FOLLOW;
        }
        if (activityType == ActivityType.REPLY) {
            return MY_REPLY;
        }
        if (activityType == ActivityType.LIKE) {
            return MY_LIKE;
        }
        throw new IllegalArgumentException("Activity 타입이 NewsFeedType 과 매칭이 안됩니다");
    }
}
