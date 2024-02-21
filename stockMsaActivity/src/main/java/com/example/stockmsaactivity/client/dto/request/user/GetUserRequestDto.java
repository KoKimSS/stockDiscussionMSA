package com.example.stockmsaactivity.client.dto.request.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetUserRequestDto {

    private Long userId;

    @Builder
    private GetUserRequestDto(Long userId) {
        this.userId = userId;
    }
}
