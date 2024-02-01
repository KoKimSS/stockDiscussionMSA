package com.example.stockmsanewsfeed.web.dto.request.newsFeed;

import com.example.stockmsanewsfeed.domain.newsFeed.ActivityType;
import com.example.stockmsanewsfeed.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CreateNewsFeedRequestDto extends RequestDto {
    private Long userId;
    ActivityType activityType;
    private Long relatedUserId;
    private Long relatedPosterId;  // 해당 활동이 포함된 글

    @Builder
    private CreateNewsFeedRequestDto(Long userId, ActivityType activityType, Long relatedUserId, Long relatedPosterId) {
        this.userId = userId;
        this.activityType = activityType;
        this.relatedUserId = relatedUserId;
        this.relatedPosterId = relatedPosterId;
    }
}
