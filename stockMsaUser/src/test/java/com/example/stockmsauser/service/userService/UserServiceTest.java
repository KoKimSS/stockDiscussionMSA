package com.example.stockmsauser.service.userService;

import com.example.stockmsauser.common.ResponseCode;
import com.example.stockmsauser.common.ResponseMessage;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdatePasswordRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdateProfileRequestDto;
import com.example.stockmsauser.web.dto.response.user.GetUserResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdatePasswordResponseDto;
import com.example.stockmsauser.web.dto.response.user.UpdateProfileResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserJpaRepository userJpaRepository;

    @AfterEach
    public void afterEach() {
        userJpaRepository.deleteAllInBatch();
    }

    @DisplayName("패스워드 업데이트 서비스")
    @Test
    public void updatePasswordWithMatchedPassword() throws Exception {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder().name("user").password(encodedPassword).build();
        userJpaRepository.save(user);

        String newPassword = "newpassword123";

        UpdatePasswordRequestDto requestDto = UpdatePasswordRequestDto.
                builder().userId(user.getId())
                .password(password)
                .newPassword(newPassword)
                .build();

        //when
        ResponseEntity<? super UpdatePasswordResponseDto> response = userService.updatePassword(requestDto);

        //then
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @DisplayName("기존과 매칭되지 않는 비밀번호를 이용해 새로운 비밀번호를 업데이트를 하여 업데이트 실패 ")
    @Test
    public void updatePasswordWithUnmatchedPassword() throws Exception {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "password123";
        String wrongPassword = "password456";
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder().name("user").password(encodedPassword).build();
        userJpaRepository.save(user);

        String newPassword = "newpassword123";

        UpdatePasswordRequestDto requestDto = UpdatePasswordRequestDto.
                builder().userId(user.getId())
                .password(wrongPassword)
                .newPassword(newPassword)
                .build();

        //when
        ResponseEntity<? super UpdatePasswordResponseDto> response = userService.updatePassword(requestDto);

        //then
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
    }

    @DisplayName("프로필 업데이트 서비스")
    @Test
    public void updateProfile() throws Exception {
        //given
        User user = User.builder()
                .name("user")
                .imgPath("imgPath")
                .introduction("introduction").build();
        User save = userJpaRepository.save(user);
        Long userId = save.getId();


        String updateName = "업데이트이름";
        String updateImgPath = "업데이트이미지";
        String updateIntro = "업데이트자기소개";

        UpdateProfileRequestDto requestDto = UpdateProfileRequestDto.
                builder().userId(user.getId())
                .name(updateName)
                .imgPath(updateImgPath)
                .introduction(updateIntro)
                .build();

        //when
        ResponseEntity<? super UpdateProfileResponseDto> response = userService.updateProfile(requestDto);
        User updatedUser = userJpaRepository.findById(userId).get();

        //then
        Assertions.assertThat(updatedUser).extracting("name", "imgPath", "introduction")
                .containsExactly(updateName, updateImgPath, updateIntro);

        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @DisplayName("아이디로 유저 찾기")
    @Test
    public void findById() throws Exception {
        //given
        User user = User.builder()
                .name("user")
                .imgPath("imgPath")
                .introduction("introduction").build();
        User save = userJpaRepository.save(user);
        Long userId = save.getId();

        GetUserRequestDto requestDto = GetUserRequestDto.builder().userId(userId).build();

        //when
        ResponseEntity<? super GetUserResponseDto> response = userService.findById(requestDto);
        GetUserResponseDto responseDto = (GetUserResponseDto) response.getBody();

        //then
        Assertions.assertThat(responseDto.getUserDto())
                .extracting("name","imgPath","introduction")
                .containsExactly("user","imgPath","introduction");

        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }
}