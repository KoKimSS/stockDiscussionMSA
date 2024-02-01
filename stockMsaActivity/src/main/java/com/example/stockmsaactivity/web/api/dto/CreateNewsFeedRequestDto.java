package com.example.stockmsaactivity.web.api.dto;

import com.example.stockmsaactivity.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CreateNewsFeedRequestDto extends RequestDto {
    private Long userId;
    String activityType;
    private Long relatedUserId;
    private Long relatedPosterId;  // 해당 활동이 포함된 글

    @Builder
    private CreateNewsFeedRequestDto(Long userId, String activityType, Long relatedUserId, Long relatedPosterId) {
        this.userId = userId;
        this.activityType = activityType;
        this.relatedUserId = relatedUserId;
        this.relatedPosterId = relatedPosterId;
    }
}
