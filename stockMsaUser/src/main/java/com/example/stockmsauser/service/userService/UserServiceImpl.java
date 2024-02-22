package com.example.stockmsauser.service.userService;


import com.example.stockmsauser.common.error.exception.CertificationFailException;
import com.example.stockmsauser.common.error.exception.DatabaseErrorException;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.user.GetUserRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdatePasswordRequestDto;
import com.example.stockmsauser.web.dto.request.user.UpdateProfileRequestDto;
import com.example.stockmsauser.web.dto.response.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public Long updatePassword(UpdatePasswordRequestDto dto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Long requestUserId = dto.getUserId();
        User user = userJpaRepository.findById(requestUserId)
                .orElseThrow(() -> new DatabaseErrorException("db 에러"));
        String password = dto.getPassword();
        String newPassword = dto.getNewPassword();
        if (!passwordEncoder.matches(password, user.getPassword())) throw new CertificationFailException("인증 실패");

        // 새로운 비밀번호 해싱
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(hashedPassword);

        /**
         * 기존의 인증은 모두 폐기 해야 한다 !!
         */
        return user.getId();
    }

    @Override
    @Transactional
    public Long updateProfile(UpdateProfileRequestDto dto) {
        Long requestUserId = dto.getUserId();

        User user = userJpaRepository.findById(requestUserId)
                .orElseThrow(()->new DatabaseErrorException("DB 에러"));

        user.updateProfile(dto.getName(), dto.getImgPath(), dto.getIntroduction());

        return user.getId();
    }

    @Override
    public UserDto findById(GetUserRequestDto dto) {
        Long userId = dto.getUserId();
        User user = userJpaRepository.findById(userId).get();
        System.out.println(user.getName());
        UserDto userDto = UserDto.builder()
                .id(userId)
                .email(user.getEmail())
                .roles(user.getRoles())
                .imgPath(user.getImgPath())
                .introduction(user.getIntroduction())
                .name(user.getName()).build();
        return userDto;
    }
}
