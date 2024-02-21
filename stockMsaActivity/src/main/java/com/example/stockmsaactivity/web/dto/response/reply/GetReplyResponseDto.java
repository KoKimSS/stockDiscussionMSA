package com.example.stockmsaactivity.web.dto.response.reply;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
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

}
