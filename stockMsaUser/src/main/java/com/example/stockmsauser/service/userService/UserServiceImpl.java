package com.example.stockmsauser.service.userService;


import com.example.stockmsauser.config.jwt.JwtUtil;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdatePasswordRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdateProfileRequestDto;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import com.example.stockmsauser.web.dto.response.user.GetUserResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdatePasswordResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdateProfileResponseDto;
import com.example.stockmsauser.web.dto.response.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserJpaRepository userJpaRepository;
    @Override
    @Transactional
    public ResponseEntity<? super UpdatePasswordResponseDto> updatePassword(UpdatePasswordRequestDto dto) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            Long requestUserId = dto.getUserId();
            User user = userJpaRepository.findById(requestUserId).get();
            String password = dto.getPassword();
            String newPassword = dto.getNewPassword();
            if(!passwordEncoder.matches(password, user.getPassword())){
                return UpdateProfileResponseDto.certificationFail();
            }
            // 새로운 비밀번호 해싱
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.updatePassword(hashedPassword);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return UpdatePasswordResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super UpdateProfileResponseDto> updateProfile(UpdateProfileRequestDto dto) {
        Long requestUserId = dto.getUserId();

        try {
            Optional<User> userById = userJpaRepository.findById(requestUserId);
            if (!userById.isPresent()) {
                return UpdateProfileResponseDto.databaseError();
            }

            User user = userById.get();
            user.updateProfile(dto.getName(), dto.getImgPath(), dto.getIntroduction());
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return UpdateProfileResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetUserResponseDto> findById(GetUserRequestDto dto) {
        Long userId = dto.getUserId();
        User user = userJpaRepository.findById(userId).get();
        System.out.println(user.getName());
        return GetUserResponseDto.success(UserDto.builder()
                .id(userId)
                .email(user.getEmail())
                .roles(user.getRoles())
                .imgPath(user.getImgPath())
                .introduction(user.getIntroduction())
                .name(user.getName()).build()
        );
    }
}
