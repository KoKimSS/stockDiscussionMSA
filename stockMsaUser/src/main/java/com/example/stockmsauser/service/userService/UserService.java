package com.example.stockmsauser.service.userService;

import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdatePasswordRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdateProfileRequestDto;
import com.example.stockmsauser.web.dto.response.user.GetUserResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdatePasswordResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdateProfileResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?super UpdatePasswordResponseDto> updatePassword(UpdatePasswordRequestDto dto);

    ResponseEntity<? super UpdateProfileResponseDto> updateProfile(UpdateProfileRequestDto dto);

    ResponseEntity<? super GetUserResponseDto> findById(GetUserRequestDto dto);
}
