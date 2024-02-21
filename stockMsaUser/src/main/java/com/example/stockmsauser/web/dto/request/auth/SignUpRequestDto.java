package com.example.stockmsauser.web.dto.request.auth;

import com.example.stockmsauser.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.example.stockmsauser.common.error.ValidationMessage.*;


@Data
@NoArgsConstructor
public class SignUpRequestDto extends RequestDto {

    @NotBlank(message = NOT_BLANK_EMAIL)
    @Email(message = NOT_EMAIL)
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$"
    ,message = NOT_PASSWORD)
    private String password;

    @NotBlank(message = NOT_BLANK_NAME)
    private String name;
    @NotBlank(message = NOT_BLANK_IMAGE)
    private String imgPath;

    @NotBlank(message = NOT_BLANK_INTRO)
    private String introduction;

    @NotBlank(message = NOT_BLANK_TOKEN)
    private String certificationNumber;

    @Builder
    private SignUpRequestDto(String email, String password, String name, String imgPath, String introduction, String certificationNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imgPath = imgPath;
        this.introduction = introduction;
        this.certificationNumber = certificationNumber;
    }
}
