package com.example.stockmsauser.service.authService;


import com.example.stockmsauser.common.error.ResponseCode;
import com.example.stockmsauser.common.error.ResponseMessage;
import com.example.stockmsauser.config.jwt.JwtUtil;
import com.example.stockmsauser.domain.certification.Certification;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.provider.EmailProvider;
import com.example.stockmsauser.repository.certificationRepository.CertificationJpaRepository;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import com.example.stockmsauser.web.dto.request.auth.CheckCertificationRequestDto;
import com.example.stockmsauser.web.dto.request.auth.EmailCertificationRequestDto;
import com.example.stockmsauser.web.dto.request.auth.EmailCheckRequestDto;
import com.example.stockmsauser.web.dto.request.auth.SignUpRequestDto;
import com.example.stockmsauser.web.dto.response.ResponseDto;
import com.example.stockmsauser.web.dto.response.auth.CheckCertificationResponseDto;
import com.example.stockmsauser.web.dto.response.auth.EmailCertificationResponseDto;
import com.example.stockmsauser.web.dto.response.auth.EmailCheckResponseDto;
import com.example.stockmsauser.web.dto.response.auth.SignUpResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserJpaRepository userJpaRepository;
    private final EmailProvider emailProvider;
    private final CertificationJpaRepository certificationJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    public static final int TimeValid = 2;
    public static final String REDIS_BLACKLIST_KEY = "jwt-blacklist:";
    @Override
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto) {
        try {
            String email = dto.getEmail();
            boolean isExistEmail = userJpaRepository.existsByEmail(email);
            if(isExistEmail) return EmailCheckResponseDto.duplicateEmail();
        } catch (Exception exception) {
            exception.printStackTrace();
            return EmailCheckResponseDto.databaseError();
        }
        return EmailCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String email = dto.getEmail();

            boolean isExistEmail = userJpaRepository.existsByEmail(email);
            if(isExistEmail) return EmailCertificationResponseDto.duplicateEmail();

            //기존에 인증시도 기록이 있으면 삭제하고 생성
            Optional<Certification> existByEmail = certificationJpaRepository.findByEmail(email);
            if(existByEmail.isPresent()){
                certificationJpaRepository.delete(existByEmail.get());
            }
            String certificationNumber = createCertificationNumber();
            boolean isEmailSendSuccessful = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isEmailSendSuccessful) return EmailCertificationResponseDto.mailSendFail();

            Certification certification = Certification.builder().email(email).certificationNumber(certificationNumber).build();
            certificationJpaRepository.save(certification);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();
            LocalDateTime certificateTime = dto.getCertificateTime();

            Optional<Certification> byUserEmail = certificationJpaRepository.findByEmail(email);
            if(!byUserEmail.isPresent()) return CheckCertificationResponseDto.certificationFail();

            Certification certification = byUserEmail.get();

            boolean isMatch = isDtoMatchCertification(email, certificationNumber, certification);
            if(!isMatch) return CheckCertificationResponseDto.certificationFail();

            boolean timeValid = isCertificationTimeValid(certification,certificateTime);
            if(!timeValid) return CheckCertificationResponseDto.certificationExpired();
            certification.certificated();
            System.out.println("certification = " + certification.getIsCertified());

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> singUp(SignUpRequestDto dto) {

        try {
            String email = dto.getEmail();
            boolean existsByEmail = userJpaRepository.existsByEmail(email);
            if(existsByEmail) return SignUpResponseDto.duplicateEmail();

            String certificationNumber = dto.getCertificationNumber();
            Optional<Certification> optionalCertificationByEmail = certificationJpaRepository.findByEmail(email);

            if(!optionalCertificationByEmail.isPresent()) SignUpResponseDto.certificationFail();
            Certification certificationByEmail = optionalCertificationByEmail.get();

            if(!isDtoMatchCertification(email,certificationNumber,certificationByEmail))
                return SignUpResponseDto.certificationFail();

            String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
            User user = User.builder()
                    .email(email)
                    .password(encodedPassword)
                    .name(dto.getName())
                    .introduction(dto.getIntroduction())
                    .imgPath(dto.getImgPath())
                    .build();

            userJpaRepository.save(user);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super ResponseDto> logOut(String token) {

        Date expirationDate = JwtUtil.getExpirationDateFromToken(token);
        if (expirationDate != null) {
            long expirationMillis = Math.max(0, expirationDate.getTime() - System.currentTimeMillis());
            addJwtTokenToBlackListRedis(token, expirationMillis);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @Transactional
    @Override
    public void addJwtTokenToBlackListRedis(String token, long expirationMillis) {
        String key = REDIS_BLACKLIST_KEY + token;
        System.out.println("Redis 호출");
        redisTemplate.opsForValue().set(key, "blacklisted", expirationMillis, TimeUnit.MILLISECONDS);
    }

    private static boolean isDtoMatchCertification(String email, String certificationNumber, Certification certification) {
        return certification.getEmail().equals(email) && certificationNumber.equals(certification.getCertificationNumber());
    }

    private String createCertificationNumber() {
        String certificationNumber = "";
        //4자리 숫자로 구성된 번호
        for (int count = 0; count < 4; count++) certificationNumber += (int) (Math.random() * 10);
        return certificationNumber;
    }

    private boolean isCertificationTimeValid(Certification certification,LocalDateTime certificateTime) {
        LocalDateTime certificationCreationTime = certification.getCreatedDate();
        Duration duration = Duration.between(certificationCreationTime, certificateTime);
        return duration.toSeconds() <= TimeValid*60;
    }
}
