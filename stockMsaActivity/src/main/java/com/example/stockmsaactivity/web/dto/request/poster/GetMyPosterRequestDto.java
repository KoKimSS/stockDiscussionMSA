package com.example.stockmsaactivity.web.dto.request.poster;


import com.example.stockmsaactivity.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.example.stockmsaactivity.common.ValidationMessage.NOT_NULL_USER;


@Getter
@NoArgsConstructor
public class GetMyPosterRequestDto extends RequestDto {
    @NotNull(message = NOT_NULL_USER)
    private Long userId;

    @Builder
    private GetMyPosterRequestDto(Long userId) {
        this.userId = userId;
    }
}
