package com.example.stockmsaactivity.web.dto.response.reply;

import com.example.stockmsaactivity.common.ResponseCode;
import com.example.stockmsaactivity.common.ResponseMessage;
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

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
