package com.example.stockmsauser.service.authService;


import com.example.stockmsauser.common.error.exception.*;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public static final int TimeValid = 2;
    public static final String REDIS_BLACKLIST_KEY = "jwt-blacklist:";
    private final UserJpaRepository userJpaRepository;
    private final EmailProvider emailProvider;
    private final CertificationJpaRepository certificationJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    private static boolean isDtoMatchCertification(String email, String certificationNumber, Certification certification) {
        return certification.getEmail().equals(email) && certificationNumber.equals(certification.getCertificationNumber());
    }

    @Override
    public boolean emailCheck(EmailCheckRequestDto dto) {
        String email = dto.getEmail();
        boolean isExistEmail = userJpaRepository.existsByEmail(email);
        return isExistEmail;
    }

    @Override
    @Transactional
    public boolean emailCertification(EmailCertificationRequestDto dto) {
        String email = dto.getEmail();

        boolean isExistEmail = userJpaRepository.existsByEmail(email);
        if (isExistEmail) throw new DuplicateMailException("메일 중복");

        //기존에 인증시도 기록이 있으면 삭제하고 생성
        Optional<Certification> existByEmail = certificationJpaRepository.findByEmail(email);
        if (existByEmail.isPresent()) {
            certificationJpaRepository.delete(existByEmail.get());
        }
        String certificationNumber = createCertificationNumber();
        boolean isEmailSendSuccessful = emailProvider.sendCertificationMail(email, certificationNumber);
        if (!isEmailSendSuccessful) throw new MailSendFailException("메일 전송 실패");

        Certification certification = Certification.builder().email(email).certificationNumber(certificationNumber).build();
        certificationJpaRepository.save(certification);

        return true;
    }

    @Override
    @Transactional
    public boolean checkCertification(CheckCertificationRequestDto dto) {
        String email = dto.getEmail();
        String certificationNumber = dto.getCertificationNumber();
        LocalDateTime certificateTime = dto.getCertificateTime();

        Certification certification = certificationJpaRepository.findByEmail(email)
                .orElseThrow(() -> new CertificationFailException("인증서 존재 x"));

        boolean isMatch = isDtoMatchCertification(email, certificationNumber, certification);
        if (!isMatch) throw new CertificationFailException("인증 실패");

        boolean timeValid = isCertificationTimeValid(certification, certificateTime);
        if (!timeValid) throw new CertificationExpiredException("시간 초과");

        certification.certificated();
        return true;
    }

    @Override
    @Transactional
    public Long singUp(SignUpRequestDto dto) {

        String email = dto.getEmail();
        boolean existsByEmail = userJpaRepository.existsByEmail(email);
        if (existsByEmail) throw new DuplicateMailException("이메일 중복");

        String certificationNumber = dto.getCertificationNumber();
        Certification certificationByEmail = certificationJpaRepository.findByEmail(email)
                .orElseThrow(()->new CertificationFailException("인증 실패"));

        if (!isDtoMatchCertification(email, certificationNumber, certificationByEmail))
            throw new CertificationFailException("인증 실패");

        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .name(dto.getName())
                .introduction(dto.getIntroduction())
                .imgPath(dto.getImgPath())
                .build();

        User save = userJpaRepository.save(user);
        return save.getId();
    }

    @Override
    public boolean logOut(String token) {
        try {
            Date expirationDate = JwtUtil.getExpirationDateFromToken(token);
            if (expirationDate != null) {
                long expirationMillis = Math.max(0, expirationDate.getTime() - System.currentTimeMillis());
                addJwtTokenToBlackListRedis(token, expirationMillis);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("내부 에러");
        }
        return true;
    }

    @Transactional
    @Override
    public void addJwtTokenToBlackListRedis(String token, long expirationMillis) {
        String key = REDIS_BLACKLIST_KEY + token;
        redisTemplate.opsForValue().set(key, "blacklisted", expirationMillis, TimeUnit.MILLISECONDS);
    }

    private String createCertificationNumber() {
        String certificationNumber = "";
        //4자리 숫자로 구성된 번호
        for (int count = 0; count < 4; count++) certificationNumber += (int) (Math.random() * 10);
        return certificationNumber;
    }

    private boolean isCertificationTimeValid(Certification certification, LocalDateTime certificateTime) {
        LocalDateTime certificationCreationTime = certification.getCreatedDate();
        Duration duration = Duration.between(certificationCreationTime, certificateTime);
        return duration.toSeconds() <= TimeValid * 60;
    }
}
