package com.example.stockmsauser.repository.userRepository;

import com.example.stockmsauser.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserJpaRepositoryTest {
    @Autowired
    private UserJpaRepository userJpaRepository;


    @DisplayName("이메일로 유저를 찾는다")
    @Test
    public void findByEmail() throws Exception {
        //given
        String email = "exist@email.com";
        User user = User.builder().email(email).build();
        userJpaRepository.save(user);

        //when
        User userByEmail = userJpaRepository.findByEmail(email).get();

        //then
        assertThat(userByEmail).isEqualTo(user);
    }

    @DisplayName("이메일로 유저 존재유무를 찾는다")
    @Test
    public void existsByEmail() throws Exception {
        //given
        String email = "email@email.com";
        User user = User.builder().email(email).build();
        userJpaRepository.save(user);

        //when
        boolean existsByEmail = userJpaRepository.existsByEmail(email);

        //then
        assertTrue(existsByEmail);
    }
}