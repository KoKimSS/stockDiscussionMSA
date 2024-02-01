package com.example.stockmsauser.web.dto.request.auth;

import com.example.stockmsauser.common.ValidationMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@NoArgsConstructor
public class EmailCertificationRequestDto {

    @Email(message = ValidationMessage.NOT_EMAIL)
    @NotBlank(message = ValidationMessage.NOT_BLANK_EMAIL)
    private String email;

    @Builder
    private EmailCertificationRequestDto(String email) {
        this.email = email;
    }
}
