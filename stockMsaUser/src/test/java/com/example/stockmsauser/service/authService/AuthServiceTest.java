package com.example.stockmsauser.service.authService;

import com.example.stockmsauser.common.error.exception.CertificationExpiredException;
import com.example.stockmsauser.common.error.exception.CertificationFailException;
import com.example.stockmsauser.common.error.exception.DuplicateMailException;
import com.example.stockmsauser.config.TestRedisConfig;
import com.example.stockmsauser.config.jwt.JwtUtil;
import com.example.stockmsauser.domain.certification.Certification;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.certificationRepository.CertificationJpaRepository;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.auth.CheckCertificationRequestDto;
import com.example.stockmsauser.web.dto.request.auth.EmailCertificationRequestDto;
import com.example.stockmsauser.web.dto.request.auth.EmailCheckRequestDto;
import com.example.stockmsauser.web.dto.request.auth.SignUpRequestDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import static com.example.stockmsauser.service.authService.AuthServiceImpl.REDIS_BLACKLIST_KEY;
import static com.example.stockmsauser.service.authService.AuthServiceImpl.TimeValid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Transactional
@Import(TestRedisConfig.class)
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private CertificationJpaRepository certificationJpaRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;



    @DisplayName("이메일이 중복되어 있는 경우")
    @Test
    public void emailCheckWithDuplicatedMail(){
        //given
        String email = "email@email.com";
        User user = createUser(email);
        userJpaRepository.save(user);
        EmailCheckRequestDto requestDto = createEmailCheckRequestDto(email);

        //when
        boolean isDuplicate = authService.emailCheck(requestDto);

        //then
        assertThat(isDuplicate).isTrue();
    }

    @DisplayName("이메일이 중복되지 않은 경우")
    @Test
    public void emailCheckWithNewMail(){
        //given
        String email = "email@email.com";
        String newEmail = "newEmail@gmail.com";
        User user = createUser(email);
        userJpaRepository.save(user);
        EmailCheckRequestDto requestDto = createEmailCheckRequestDto(newEmail);

        //when
        boolean isDuplicate = authService.emailCheck(requestDto);

        //then
        assertThat(isDuplicate).isFalse();
    }

    @DisplayName("중복된 이메일인 경우 이메일 인증 코드 발행 실패")
    @Test
    public void emailCertificationWithDuplicateEmail() {
        //given
        String email = "email@email.com";
        User user = createUser(email);
        userJpaRepository.save(user);
        EmailCertificationRequestDto duplicatedRequestDto = getEmailCertificationRequestDto(email);

        //when, then
        assertThrows(DuplicateMailException.class,
                ()->authService.emailCertification(duplicatedRequestDto));
    }

    @DisplayName("새로운 이메일인 경우 이메일 인증 코드 발행 성공")
    @Test
    public void emailCertification_with_newEmail() {
        //given
        String email = "email@email.com";
        String newEmail = "newEmail@email.com";
        User user = createUser(email);
        userJpaRepository.save(user);
        EmailCertificationRequestDto successRequestDto = getEmailCertificationRequestDto(newEmail);

        //when
        boolean isSuccess = authService.emailCertification(successRequestDto);

        //then
        assertThat(isSuccess).isTrue();
    }

    @DisplayName("올바른 토큰, 이메일, 요청시간 으로 인증 성공")
    @Test
    public void checkCertificationWithValidRequest() {
        //given
        String email = "email@naver.com";
        String number = "1234";
        LocalDateTime successTime = LocalDateTime.now().plus(Duration.ofMinutes(TimeValid));

        Certification certification = createCertification(email, number);
        certificationJpaRepository.save(certification);

        CheckCertificationRequestDto successRequest = getCheckCertificationRequestDto(email, number, successTime);
        //when
        boolean isSuccess = authService.checkCertification(successRequest);
        boolean isCertified = certificationJpaRepository.findByEmail(email).get().getIsCertified();

        //then
        assertThat(isCertified).isTrue();
        assertThat(isSuccess).isTrue();
    }

    @DisplayName("시간 초과로 인증 실패")
    @Test
    public void checkCertificationWithExceedTime() {
        //given
        String email = "email@naver.com";
        String number = "1234";
        LocalDateTime failTime = LocalDateTime.now().plus(Duration.ofMinutes(TimeValid + 1));

        Certification certification = createCertification(email, number);
        certificationJpaRepository.save(certification);
        CheckCertificationRequestDto timeExceedRequest = getCheckCertificationRequestDto(email, number, failTime);

        //when, then

        assertThrows(CertificationExpiredException.class, () -> authService.checkCertification(timeExceedRequest));
        Boolean isCertified = certificationJpaRepository.findByEmail(email).get().getIsCertified();
        assertThat(isCertified).isFalse();
    }

    @DisplayName("잘못된 메일로 인증 실패")
    @Test
    public void checkCertificationWithWrongEmail() {
        //given
        String email = "email@naver.com";
        String number = "1234";
        String wrongEmail = "wrong@naver.com";
        LocalDateTime successTime = LocalDateTime.now().plus(Duration.ofMinutes(TimeValid));

        Certification certification = createCertification(email, number);
        certificationJpaRepository.save(certification);

        CheckCertificationRequestDto wrongEmailRequest = getCheckCertificationRequestDto(wrongEmail, number, successTime);
        //when, then

        assertThrows(CertificationFailException.class,()->authService.checkCertification(wrongEmailRequest));
        Boolean isCertified = certificationJpaRepository.findByEmail(email).get().getIsCertified();
        assertThat(isCertified).isFalse();
    }

    @DisplayName("잘못된 번호로 인증 실패")
    @Test
    public void checkCertificationWithWrongNumber() {
        //given
        String email = "email@naver.com";
        String number = "1234";
        String wrongNumber = "5678";
        LocalDateTime successTime = LocalDateTime.now().plus(Duration.ofMinutes(TimeValid));

        Certification certification = createCertification(email, number);
        certificationJpaRepository.save(certification);

        CheckCertificationRequestDto wrongNumberRequest = getCheckCertificationRequestDto(email, wrongNumber, successTime);

        //when, then
        assertThrows(CertificationFailException.class,()->authService.checkCertification(wrongNumberRequest));
        Boolean isCertified = certificationJpaRepository.findByEmail(email).get().getIsCertified();
        assertThat(isCertified).isFalse();
    }

    @DisplayName("유효한 이메일, 토큰 번호, 토큰 인증 성공 상태로 회원가입 성공")
    @Test
    public void singUpWithValidRequest() {
        //given
        String number = "1234";
        String email = "email@naver.com";
        Certification certification = createCertification(email, number);
        certificationJpaRepository.save(certification);
        certification.certificated();
        SignUpRequestDto successRequestDto = getSignUpRequestDto(number, email);

        //when
        Long userId = authService.singUp(successRequestDto);

        //then
        assertThat(userId).isNotNull();
    }

    @DisplayName("인증된 메일과 다른 이메일로 회원가입 실패")
    @Test
    public void singUpWithWrongEmail() {
        //given
        String number = "1234";
        String email = "email@naver.com";
        String wrongEmail = "wrong@naver.com";
        Certification certification = createCertification(email, number);

        certification.certificated();
        certificationJpaRepository.save(certification);
        SignUpRequestDto wrongEmailRequestDto = getSignUpRequestDto(number, wrongEmail);

        //when, then
        assertThrows(CertificationFailException.class,()->authService.singUp(wrongEmailRequestDto));
    }

    @DisplayName("인증된 토큰 번호와 다른 토큰으로 회원가입 실패")
    @Test
    public void singUp() throws Exception {
        //given
        String number = "1234";
        String email = "email@naver.com";
        String wrongNumber = "5678";
        Certification certification = createCertification(email, number);
        certificationJpaRepository.save(certification);
        certification.certificated();
        SignUpRequestDto wrongNumberRequestDto = getSignUpRequestDto(wrongNumber, email);

        //when, then
        assertThrows(CertificationFailException.class,()->authService.singUp(wrongNumberRequestDto));
    }

    @Test
    @DisplayName("로그인이 되어 있는 경우 JWT 블랙리스트에 현재 토큰 추가")
    public void logOut() throws Exception {
        // Given
        String token = "your_token";
        long expirationMillis = 1000L; // 예시로 1초
        Date expirationDate = new Date(System.currentTimeMillis() + expirationMillis);
        MockedStatic<JwtUtil> jwtUtil = mockStatic(JwtUtil.class);
        given(JwtUtil.getExpirationDateFromToken(any(String.class))).willReturn(expirationDate);
        String key = REDIS_BLACKLIST_KEY + token;

        // When
        boolean isSuccess = authService.logOut(token);
        boolean keyExists = redisTemplate.hasKey(key);

        // Then
        assertThat(keyExists).isTrue();
        assertThat(isSuccess).isTrue();
        jwtUtil.close();
    }

    private static SignUpRequestDto getSignUpRequestDto(String number, String email) {
        return SignUpRequestDto.builder()
                .email(email)
                .password("12345678")
                .name("유저")
                .introduction("안녕하세요")
                .imgPath("img_path")
                .certificationNumber(number).build();
    }

    private static User createUser(String email) {
        return User.builder().name("user").email(email).build();
    }

    private static EmailCheckRequestDto createEmailCheckRequestDto(String email) {
        return EmailCheckRequestDto.builder().email(email).build();
    }

    private static EmailCertificationRequestDto getEmailCertificationRequestDto(String email) {
        return EmailCertificationRequestDto.builder().email(email).build();
    }

    private static CheckCertificationRequestDto getCheckCertificationRequestDto(String email, String number, LocalDateTime successTime) {
        return CheckCertificationRequestDto.builder().email(email)
                .certificationNumber(number).certificateTime(successTime).build();
    }

    private Certification createCertification(String email, String number) {
        return Certification.builder().email(email)
                .certificationNumber(number)
                .build();
    }
}