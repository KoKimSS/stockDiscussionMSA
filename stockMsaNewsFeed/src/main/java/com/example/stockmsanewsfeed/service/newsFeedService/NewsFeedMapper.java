package com.example.stockmsanewsfeed.service.newsFeedService;


import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeed;
import com.example.stockmsanewsfeed.domain.newsFeed.NewsFeedType;
import com.example.stockmsanewsfeed.web.api.dto.request.activity.GetPosterRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.request.user.GetUserRequestDto;
import com.example.stockmsanewsfeed.web.api.dto.response.PosterDto;
import com.example.stockmsanewsfeed.web.api.dto.response.user.UserDto;
import com.example.stockmsanewsfeed.web.api.user.UserApi;
import com.example.stockmsanewsfeed.web.api.activity.ActivityApi;
import com.example.stockmsanewsfeed.web.dto.response.newsFeed.NewsFeedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsFeedMapper {

    private final UserApi userApi;
    private final ActivityApi activityApi;
    public NewsFeedDto toGetMyNewsFeedDto(NewsFeed newsFeed) {
        Long userId = newsFeed.getUserId();
        NewsFeedType newsFeedType = newsFeed.getNewsFeedType();
        Long activityUserId = newsFeed.getActivityUserId();
        Long relatedUserId = newsFeed.getRelatedUserId();
        Long relatedPosterId = newsFeed.getRelatedPosterId();

        UserDto userDto = userApi.getUserById(GetUserRequestDto.builder().userId(userId).build()).getUserDto();
        UserDto activityUserDto = userApi.getUserById(GetUserRequestDto.builder().userId(activityUserId).build()).getUserDto();
        UserDto relatedUserDto = null;
        if(relatedUserId!=null) {
            relatedUserDto = userApi.getUserById(GetUserRequestDto.builder().userId(relatedUserId).build()).getUserDto();
        }
        PosterDto posterDto=null;
        if(relatedPosterId!=null) {
            posterDto = activityApi.getPoster(GetPosterRequestDto.builder().posterId(relatedPosterId).build());
        }
        NewsFeedDto dto = NewsFeedDto.builder()
                .userId(userId)
                .userName(userDto.getName())
                .activityUserId(activityUserId)
                .activityUserName(activityUserDto.getName())
                .relatedUserId((relatedUserId != null) ? relatedUserId : null)
                .relatedUserName((relatedUserDto != null) ? relatedUserDto.getName() : null)
                .relatedPosterId((relatedPosterId != null) ? relatedPosterId : null)
                .relatedPosterName((posterDto != null) ? posterDto.getTitle() : null)
                .newsFeedType(newsFeedType).build();
        System.out.println(dto.getMessage());
        return dto;
    }
}
