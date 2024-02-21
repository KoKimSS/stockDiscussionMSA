package com.example.stockmsaactivity.web.dto.response.poster;


import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
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

}
