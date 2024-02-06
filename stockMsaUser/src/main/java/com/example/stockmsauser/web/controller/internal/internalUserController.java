package com.example.stockmsauser.web.controller.internal;

import com.example.stockmsauser.service.userService.UserService;
import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.response.user.GetUserResponseDto;
import lombok.RequiredArgsConstructor;
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
    ResponseEntity<? super GetUserResponseDto> findById(
            @RequestBody @Valid GetUserRequestDto requestBody
    ) {
        ResponseEntity<? super GetUserResponseDto> response = userService.findById(requestBody);
        return response;
    }
}
