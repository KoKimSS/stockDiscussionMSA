package com.example.stockmsaactivity.web.dto.response.poster;


import com.example.stockmsaactivity.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CreatePosterResponseDto extends ResponseDto {

    public CreatePosterResponseDto() {
        super();
    }

    public static ResponseEntity<? super CreatePosterResponseDto> success() {
        CreatePosterResponseDto responseBody = new CreatePosterResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
