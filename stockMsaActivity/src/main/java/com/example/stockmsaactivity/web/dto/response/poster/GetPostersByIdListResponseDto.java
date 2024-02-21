package com.example.stockmsaactivity.web.dto.response.poster;


import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import com.example.stockmsaactivity.web.dto.request.RequestDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Data
@NoArgsConstructor
public class GetPostersByIdListResponseDto extends RequestDto {

    private List<PosterDto> posterList;
    public GetPostersByIdListResponseDto(List<PosterDto> posterList) {
        super();
        this.posterList=posterList;
    }

    public static ResponseEntity<? super GetPostersByIdListResponseDto> success(List<PosterDto> posterList) {
        GetPostersByIdListResponseDto responseBody = new GetPostersByIdListResponseDto(posterList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
