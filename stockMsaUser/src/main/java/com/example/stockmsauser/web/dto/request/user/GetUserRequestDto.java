package com.example.stockmsauser.web.dto.request.user;

import com.example.stockmsauser.common.error.ValidationMessage;
import com.example.stockmsauser.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
public class GetUserRequestDto extends RequestDto {
    @NotNull(message = ValidationMessage.NOT_NULL_USER)
    private Long userId;
    @Builder
    public GetUserRequestDto(Long userId) {
        this.userId = userId;
    }
}
