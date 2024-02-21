package com.example.stockmsauser.web.dto.request.follow;

import com.example.stockmsauser.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.example.stockmsauser.common.error.ValidationMessage.NOT_NULL_FOLLOWER;
import static com.example.stockmsauser.common.error.ValidationMessage.NOT_NULL_FOLLOWING;


@Getter
@NoArgsConstructor
public class StartFollowRequestDto extends RequestDto {
    @NotNull(message = NOT_NULL_FOLLOWER)
    private Long followerId;
    @NotNull(message = NOT_NULL_FOLLOWING)
    private Long followingId;

    @Builder
    private StartFollowRequestDto(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
