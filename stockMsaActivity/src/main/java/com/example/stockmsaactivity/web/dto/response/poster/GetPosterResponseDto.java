package com.example.stockmsaactivity.web.dto.response.poster;


import com.example.stockmsaactivity.common.ResponseCode;
import com.example.stockmsaactivity.common.ResponseMessage;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetPosterResponseDto extends ResponseDto {

    private PosterDto poster;
    public GetPosterResponseDto(PosterDto poster) {
        super();
        this.poster=poster;
    }

    public static ResponseEntity<? super GetPosterResponseDto> success(PosterDto poster) {
        GetPosterResponseDto responseBody = new GetPosterResponseDto(poster);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
