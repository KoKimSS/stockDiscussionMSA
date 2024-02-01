package com.example.stockmsaactivity.web.dto.request.poster;


import com.example.stockmsaactivity.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.example.stockmsaactivity.common.ValidationMessage.*;


@Data
@NoArgsConstructor
public class CreatePosterRequestDto extends RequestDto {
    @NotNull(message = NOT_NULL_USER)
    private Long userId;
    @NotBlank(message = NOT_BLANK_TITLE)
    private String title;
    @NotBlank(message = NOT_BLANK_CONTENTS)
    private String contents;

    @Builder
    private CreatePosterRequestDto(Long userId, String title, String contents) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
    }
}
