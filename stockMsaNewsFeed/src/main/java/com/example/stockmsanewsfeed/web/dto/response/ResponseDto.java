package com.example.stockmsanewsfeed.web.dto.response;

import com.example.stockmsanewsfeed.common.error.ResponseCode;
import com.example.stockmsanewsfeed.common.error.ResponseMessage;
import lombok.*;


@Getter
@RequiredArgsConstructor(staticName = "of")
@ToString
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