package com.example.stockmsaactivity.web.dto.response.likes;


import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CreateLikesResponseDto extends ResponseDto {
    public CreateLikesResponseDto() {
        super();
    }

    public static ResponseEntity<CreateLikesResponseDto> success() {
        CreateLikesResponseDto responseBody = new CreateLikesResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseEntity<ResponseDto> response = ResponseDto.databaseError();
        return response;
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseEntity<ResponseDto> response = ResponseDto.certificationFail();
        return response;
    }
}
