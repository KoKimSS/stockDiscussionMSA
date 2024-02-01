package com.example.stockmsauser.web.dto.response.user;

import com.example.stockmsauser.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class UpdateProfileResponseDto extends ResponseDto {

    public UpdateProfileResponseDto() {
        super();
    }

    public static ResponseEntity<UpdateProfileResponseDto> success() {
        UpdateProfileResponseDto responseBody = new UpdateProfileResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseEntity<ResponseDto> response = ResponseDto.databaseError();
        return response;
    }

}
