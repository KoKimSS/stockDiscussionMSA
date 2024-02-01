package com.example.stockmsanewsfeed.web.api.dto.response.user;

import com.example.stockmsanewsfeed.web.api.dto.response.ApiResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserResponseDto extends ApiResponseDto {
    String code;
    String message;
    UserDto userDto;
}
