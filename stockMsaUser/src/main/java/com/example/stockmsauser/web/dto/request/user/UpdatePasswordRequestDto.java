package com.example.stockmsauser.web.dto.request.user;

import com.example.stockmsauser.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.example.stockmsauser.common.error.ValidationMessage.*;


@Data
@NoArgsConstructor
public class UpdatePasswordRequestDto extends RequestDto {
    @NotNull(message = NOT_NULL_USER)
    private Long userId;
    @NotBlank(message = NOT_BLANK_PASSWORD)
    private String password;
    @NotBlank(message = NOT_BLANK_PASSWORD)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$",message = NOT_PASSWORD)
    private String newPassword;

    @Builder
    private UpdatePasswordRequestDto(Long userId, String password, String newPassword) {
        this.userId = userId;
        this.password = password;
        this.newPassword = newPassword;
    }
}
