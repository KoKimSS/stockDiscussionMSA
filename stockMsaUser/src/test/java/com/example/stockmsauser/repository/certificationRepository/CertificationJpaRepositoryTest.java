package com.example.stockmsauser.repository.certificationRepository;

import com.example.stockmsauser.domain.certification.Certification;
import com.netflix.discovery.EurekaClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CertificationJpaRepositoryTest {
    @Autowired
    private CertificationJpaRepository certificationJpaRepository;

    @MockBean
    private EurekaClient eurekaClient;


    @Test
    @DisplayName("이메일로 이메일 인증 번호 찾기")
    public void findByEmail() throws Exception {
        //given
        String email = "1234@gmail.com";
        String number = "1234";
        Certification certification = Certification.builder().certificationNumber(number)
                .email(email)
                .build();
        certificationJpaRepository.save(certification);

        //when
        Certification findByEmail = certificationJpaRepository.findByEmail(email).get();

        //then
        assertThat(findByEmail).isEqualTo(certification);
    }

    @DisplayName("사용자 이메일이 들어오면 그 이메일로 생성된 인증 제거")
    @Test
    public void deleteByEmail() throws Exception {
        //given
        String email = "1234@gmail.com";
        String number = "1234";
        Certification certification = Certification.builder().certificationNumber(number)
                .email(email)
                .build();
        certificationJpaRepository.save(certification);
        //when
        certificationJpaRepository.deleteByEmail(email);
        //then
        Optional<Certification> byEmail = certificationJpaRepository.findByEmail(email);
        assertFalse(byEmail.isPresent(), "이메일로 생성된 인증이 삭제되었어야 합니다.");
    }
}
