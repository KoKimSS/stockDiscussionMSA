package com.example.stockmsauser.web.dto.request.auth;

import com.example.stockmsauser.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static com.example.stockmsauser.common.error.ValidationMessage.*;


@Getter
@NoArgsConstructor
public class CheckCertificationRequestDto extends RequestDto {
    @NotBlank(message = NOT_BLANK_EMAIL)
    @Email(message = NOT_EMAIL)
    private String email;
    @NotBlank(message = NOT_BLANK_TOKEN)
    private String certificationNumber;
    private LocalDateTime certificateTime;

    @Builder
    public CheckCertificationRequestDto(String email, String certificationNumber, LocalDateTime certificateTime) {
        this.email = email;
        this.certificationNumber = certificationNumber;
        this.certificateTime = certificateTime;
    }

    public void setCertificateTime(LocalDateTime localDateTime) {
        this.certificateTime = localDateTime;
    }
}
