package com.example.stockmsauser.config.auth;

import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * http://localhost:8080/login 일 때 동작
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername 발동");
        System.out.println("email = " + email);
        
        Optional<User> optionalUser = userJpaRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            System.out.println("유저가 존재하지 않음");
            return null;
        }
        User user = optionalUser.get();
        System.out.println(user);
        return new PrincipalDetails(user);
    }
}
