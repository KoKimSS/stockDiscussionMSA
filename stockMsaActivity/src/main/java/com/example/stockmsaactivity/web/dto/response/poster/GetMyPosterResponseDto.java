package com.example.stockmsaactivity.web.dto.response.poster;


import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetMyPosterResponseDto extends ResponseDto {

    private List<PosterDto> posterList;
    public GetMyPosterResponseDto(List<PosterDto> posterList) {
        super();
        this.posterList=posterList;
    }

    public static ResponseEntity<? super GetMyPosterResponseDto> success(List<PosterDto> posterList) {
        GetMyPosterResponseDto responseBody = new GetMyPosterResponseDto(posterList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
