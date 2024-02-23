package com.example.stockmsaactivity.client.dto.response.user;

import com.example.stockmsaactivity.client.dto.response.ApiResponseDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GetUserResponseDto extends ApiResponseDto {
    public GetUserResponseDto(String code, String message, Object data) {
        super(code,message);
        ResponseDto.of(code, message, data);
    }
}
