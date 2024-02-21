package com.example.stockmsanewsfeed.client.dto.response.user;

import com.example.stockmsanewsfeed.client.dto.response.ApiResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserResponseDto extends ApiResponseDto {
    String code;
    String message;
    UserDto userDto;

    @Builder
    private GetUserResponseDto(String code, String message, UserDto userDto) {
        this.code = code;
        this.message = message;
        this.userDto = userDto;
    }
}
