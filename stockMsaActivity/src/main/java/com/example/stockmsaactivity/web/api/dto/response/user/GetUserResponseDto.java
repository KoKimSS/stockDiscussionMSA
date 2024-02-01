package com.example.stockmsaactivity.web.api.dto.response.user;

import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserResponseDto extends ResponseDto {
    String code;
    String message;
    UserDto userDto;
}
