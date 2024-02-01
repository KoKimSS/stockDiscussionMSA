package com.example.stockmsaactivity.web.dto.response.reply;

import com.example.stockmsaactivity.common.ResponseCode;
import com.example.stockmsaactivity.common.ResponseMessage;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetReplyResponseDto extends ResponseDto {
    private ReplyDto replyDto;
    public GetReplyResponseDto(ReplyDto replyDto) {
        super();
        this.replyDto = replyDto;
    }
    public static ResponseEntity<? super GetReplyResponseDto> success(ReplyDto replyDto) {
        GetReplyResponseDto responseBody = new GetReplyResponseDto(replyDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
