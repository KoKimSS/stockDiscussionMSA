package com.example.stockmsauser.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.stockmsauser.config.auth.PrincipalDetails;
import com.example.stockmsauser.domain.user.User;
import com.example.stockmsauser.repository.jwtBlackListRepository.JwtBlackListJpaRepository;
import com.example.stockmsauser.repository.userRepository.UserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 *  시큐리티 filter 중 BasicAuthentication Filter 가 있음
 *  - 권환이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 거침
 *  - 만약 권한이나 인증이 필요없으면 이 필터를 안 거침
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserJpaRepository userJpaRepository;
    private final JwtBlackListJpaRepository jwtBlackListJpaRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserJpaRepository userJpaRepository, JwtBlackListJpaRepository jwtBlackListJpaRepository) {
        super(authenticationManager);
        this.userJpaRepository = userJpaRepository;
        this.jwtBlackListJpaRepository = jwtBlackListJpaRepository;
    }

    /**
     *  인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        System.out.println("header : " + header);
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");
        boolean isBlackListToken = jwtBlackListJpaRepository.existsByToken(token);
        if(isBlackListToken){
            chain.doFilter(request,response);
            return;
        }
        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // 내가 SecurityContext에 집적접근해서 세션을 만들때 자동으로 UserDetailsService에 있는
        // loadByUsername이 호출됨.
        String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();
        System.out.println("email = " + email);

        if (email != null) {
            Optional<User> userByMail = userJpaRepository.findByEmail(email);
            if(!userByMail.isPresent()){
                log.info("회원가입 유저 없음");
                chain.doFilter(request, response);
                return;
            }
            User user = userByMail.get();
            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            log.info("JWT 를 통한 ID 값 검색");
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, // 나중에 컨트롤러에서 DI 해서 쓸 때 사용하기 편함.
                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
