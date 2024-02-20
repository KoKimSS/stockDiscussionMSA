package com.example.stockmsanewsfeed.common.error.handler;

import com.example.stockmsanewsfeed.common.error.ResponseCode;
import com.example.stockmsanewsfeed.common.error.exception.CertificationFailException;
import com.example.stockmsanewsfeed.common.error.exception.DatabaseErrorException;
import com.example.stockmsanewsfeed.common.error.exception.ValidationFailException;
import com.example.stockmsanewsfeed.web.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> validationExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        String errorMessage = getValidationErrorMessage(bindingResult);
        ResponseDto responseDto = new ResponseDto(ResponseCode.VALIDATION_FAIL, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(DatabaseErrorException.class)
    public ResponseEntity<ResponseDto> databaseFailExceptionHandler(DatabaseErrorException exception) {
        ResponseDto responseDto = new ResponseDto(ResponseCode.DATABASE_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    @ExceptionHandler(ValidationFailException.class)
    public ResponseEntity<ResponseDto> validationFailExceptionHandler(ValidationFailException exception) {
        ResponseDto responseDto = new ResponseDto(ResponseCode.VALIDATION_FAIL, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(CertificationFailException.class)
    public ResponseEntity<ResponseDto> certificationFailExceptionHandler(CertificationFailException exception) {
        ResponseDto responseDto = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
    }

    private String getValidationErrorMessage(BindingResult bindingResult) {
        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage()) // 필드 이름을 제외하고 에러 메시지만 가져오도록 수정
                .collect(Collectors.toList());

        return String.join(", ", errorMessages);
    }
}