package com.example.stockmsaactivity.web.dto.response;

import com.example.stockmsaactivity.common.error.ResponseCode;
import com.example.stockmsaactivity.common.error.ResponseMessage;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class ResponseDto<D> {
    private final String code;
    private final String message;
    private final D data;

    public static <D> ResponseDto<D> ofSuccess(D data) {
        return new ResponseDto<>(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }
}
