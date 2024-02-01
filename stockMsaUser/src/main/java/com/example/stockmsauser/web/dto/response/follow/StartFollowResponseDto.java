package com.example.stockmsauser.web.dto.response.follow;


import com.example.stockmsauser.common.ResponseCode;
import com.example.stockmsauser.common.ResponseMessage;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class StartFollowResponseDto extends ResponseDto {
    public StartFollowResponseDto() {
        super();
    }
    public static ResponseEntity<StartFollowResponseDto> success() {
        StartFollowResponseDto responseBody = new StartFollowResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseEntity<ResponseDto> response = ResponseDto.databaseError();
        return response;
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
