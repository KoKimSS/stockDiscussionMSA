package com.example.stockmsaactivity.web.dto.response;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ResponseDto<D> {
    private final String code;
    private final String message;
    private final D data;

    public ResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }



    public static <D> ResponseDto<D> ofSuccess(D data) {
        return new ResponseDto<>(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }
}
