package com.example.stockmsaactivity.web.dto.response.poster;


import com.example.stockmsaactivity.common.ResponseCode;
import com.example.stockmsaactivity.common.ResponseMessage;
import com.example.stockmsaactivity.web.dto.request.RequestDto;
import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.stockmsaactivity.common.ValidationMessage.NOT_NULL_POSTER;


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

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

}
