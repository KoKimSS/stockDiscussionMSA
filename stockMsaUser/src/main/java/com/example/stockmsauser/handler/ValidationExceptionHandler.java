package com.example.stockmsauser.handler;

import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> validationExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        String errorMessage = getValidationErrorMessage(bindingResult);
        ResponseDto responseDto = new ResponseDto(ResponseCode.VALIDATION_FAIL, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    private String getValidationErrorMessage(BindingResult bindingResult) {
        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage()) // 필드 이름을 제외하고 에러 메시지만 가져오도록 수정
                .collect(Collectors.toList());

        return String.join(", ", errorMessages);
    }
}
