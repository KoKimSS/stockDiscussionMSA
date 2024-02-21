package com.example.stockmsaactivity.web.dto.response.reply;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
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

}
