package com.example.stockmsauser.web.dto.request.user;

import com.example.stockmsauser.common.error.ValidationMessage;
import com.example.stockmsauser.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class UpdateProfileRequestDto extends RequestDto {
    @NotNull(message = ValidationMessage.NOT_NULL_USER)
    private Long userId;
    @NotBlank(message = ValidationMessage.NOT_BLANK_NAME)
    private String name;
    @NotBlank(message = ValidationMessage.NOT_BLANK_IMAGE)
    private String imgPath;
    @NotBlank(message = ValidationMessage.NOT_BLANK_INTRO)
    private String introduction;

    @Builder
    public UpdateProfileRequestDto(Long userId, String name, String imgPath, String introduction) {
        this.userId = userId;
        this.name = name;
        this.imgPath = imgPath;
        this.introduction = introduction;
    }
}
