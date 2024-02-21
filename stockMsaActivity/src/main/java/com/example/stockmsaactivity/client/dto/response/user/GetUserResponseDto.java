package com.example.stockmsaactivity.client.dto.response.user;

import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GetUserResponseDto extends ResponseDto {
    public GetUserResponseDto(String code, String message, Object data) {
        super(code, message, data);
    }
}
