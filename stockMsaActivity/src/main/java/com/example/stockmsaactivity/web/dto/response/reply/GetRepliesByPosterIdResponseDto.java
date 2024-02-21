package com.example.stockmsaactivity.web.dto.response.reply;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetRepliesByPosterIdResponseDto extends ResponseDto {
    private List<ReplyDto> replyDtoList;
    public GetRepliesByPosterIdResponseDto(List<ReplyDto> replyDtoList) {
        super();
        this.replyDtoList = replyDtoList;
    }
    public static ResponseEntity<? super GetRepliesByPosterIdResponseDto> success(List<ReplyDto> replyDtoList) {
        GetRepliesByPosterIdResponseDto responseBody = new GetRepliesByPosterIdResponseDto(replyDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
