package com.example.stockmsanewsfeed.client.dto.response.user;

import com.example.stockmsanewsfeed.client.dto.response.ApiResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GetUserResponseDto extends ApiResponseDto {
    UserDto userDto;

    @Builder
    private GetUserResponseDto(String code, String message, UserDto userDto) {
        super(code,message);
        this.userDto = userDto;
    }
}
