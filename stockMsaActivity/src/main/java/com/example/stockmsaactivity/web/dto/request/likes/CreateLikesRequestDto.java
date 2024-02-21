package com.example.stockmsaactivity.web.dto.request.likes;


import com.example.stockmsaactivity.common.Enum;
import com.example.stockmsaactivity.domain.like.LikeType;
import com.example.stockmsaactivity.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.example.stockmsaactivity.common.error.ValidationMessage.*;


@Data
@NoArgsConstructor
public class CreateLikesRequestDto extends RequestDto {
    @NotNull(message = NOT_NULL_USER)
    private Long userId;
    @NotNull(message = NOT_NULL_POSTER)
    private Long posterId;
//    @NotNull(message = NOT_BLANK_LIKE_TYPE)
    @Enum(enumClass = LikeType.class,message = NOT_LIKE)
    private LikeType likeType;
    private Long replyId;

    @Builder
    private CreateLikesRequestDto(Long userId, Long posterId, LikeType likeType, Long replyId) {
        this.userId = userId;
        this.posterId = posterId;
        this.likeType = likeType;
        this.replyId = replyId;
    }
}
