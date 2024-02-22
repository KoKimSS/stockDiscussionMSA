package com.example.stockmsauser.service.userService;

import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdatePasswordRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdateProfileRequestDto;
import com.example.stockmsauser.web.dto.response.user.UserDto;

public interface UserService {
    Long updatePassword(UpdatePasswordRequestDto dto);

    Long updateProfile(UpdateProfileRequestDto dto);

    UserDto findById(GetUserRequestDto dto);
}
