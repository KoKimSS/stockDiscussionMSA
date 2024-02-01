package com.example.stockmsaactivity.web.api.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String introduction;
    private String imgPath;
    private String roles; //USER, ADMIN

    @Builder
    private UserDto(Long id, String email, String password, String name, String introduction, String imgPath, String roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.introduction = introduction;
        this.imgPath = imgPath;
        this.roles = roles;
    }
}
