package com.example.stockmsanewsfeed.web.dto.request.newsFeed;

import com.example.stockmsanewsfeed.common.Enum;
import com.example.stockmsanewsfeed.common.error.ResponseMessage;
import com.example.stockmsanewsfeed.common.error.ValidationMessage;
import com.example.stockmsanewsfeed.domain.newsFeed.ActivityType;
import com.example.stockmsanewsfeed.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
public class CreateNewsFeedRequestDto extends RequestDto {
    @NotNull(message = ValidationMessage.NOT_NULL_USER)
    private Long userId;
    @Enum(message = ResponseMessage.VALIDATION_FAIL, enumClass = ActivityType.class)
    ActivityType activityType;

    // 유저가 글을 쓰는 활동은 생략 가능( 관련 유저가 없기 때문 )
    private Long relatedUserId;

    // 해당 활동이 포함된 글 id , follow 일 경우 생략 가능
    private Long relatedPosterId;

    @Builder
    private CreateNewsFeedRequestDto(Long userId, ActivityType activityType, Long relatedUserId, Long relatedPosterId) {
        this.userId = userId;
        this.activityType = activityType;
        this.relatedUserId = relatedUserId;
        this.relatedPosterId = relatedPosterId;
    }
}
