package com.example.stockmsaactivity.web.dto.response.reply;

import com.example.stockmsaactivity.common.ResponseCode;
import com.example.stockmsaactivity.common.ResponseMessage;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CreateReplyResponseDto extends ResponseDto {
    public CreateReplyResponseDto() {
        super();
    }
    public static ResponseEntity<? super CreateReplyResponseDto> success() {
        CreateReplyResponseDto responseBody = new CreateReplyResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
