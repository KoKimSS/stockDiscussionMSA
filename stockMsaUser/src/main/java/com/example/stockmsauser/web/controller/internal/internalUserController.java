package com.example.stockmsauser.web.controller.internal;

import com.example.stockmsauser.service.userService.UserService;
import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import com.example.stockmsauser.web.dto.response.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/internal/user")
@RequiredArgsConstructor
public class internalUserController {

    private final UserService userService;

    @PostMapping("/find-by-id")
    ResponseEntity<UserDto> findById(
            @RequestBody @Valid GetUserRequestDto requestBody
    ) {
        UserDto userDto = userService.findById(requestBody);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDto);
    }
}
