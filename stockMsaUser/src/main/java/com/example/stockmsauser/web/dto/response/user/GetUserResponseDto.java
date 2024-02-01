package com.example.stockmsauser.web.dto.response.user;

import com.example.stockmsauser.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetUserResponseDto extends ResponseDto {
    private UserDto userDto;
    public GetUserResponseDto(UserDto userDto) {
        super();
        this.userDto = userDto;
    }

    public static ResponseEntity<GetUserResponseDto> success(UserDto userDto) {
        GetUserResponseDto responseBody = new GetUserResponseDto(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseEntity<ResponseDto> response = ResponseDto.databaseError();
        return response;
    }
}
