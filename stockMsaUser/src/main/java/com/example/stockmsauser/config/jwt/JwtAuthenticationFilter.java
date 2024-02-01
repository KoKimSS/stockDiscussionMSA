package com.example.stockmsauser.config.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.stockmsauser.config.auth.PrincipalDetails;
import com.example.stockmsauser.domain.user.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


/**
 * 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
 * login 요청해서 username, password 전송을 하면 (post)
 * UsernamePasswordAuthenticationFilter 동작
 */

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter : 로그인 시도중");

        ObjectMapper om = new ObjectMapper();
        LoginDto loginDto = null;

        try {
            loginDto = om.readValue(request.getInputStream(), LoginDto.class);
            System.out.println("LoginDto: " + loginDto);
        } catch (IOException e) {
            System.err.println("Error reading LoginDto from request: " + e.getMessage());
            throw new AuthenticationServiceException("Error reading LoginDto from request", e);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        System.out.println("JwtAuthenticationFilter : 토큰생성완료");
        System.out.println(authenticationToken);

        try {
            // PrincipalDetailsService's loadUserByUsername() is executed here
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            System.out.println("authentication 실행 완료?");
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("principalDetails = " + principalDetails.getUser());

            if (this.postOnly && !request.getMethod().equals("POST")) {
                throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
            }

            String username = obtainUsername(request);
            username = (username != null) ? username.trim() : "";
            String password = obtainPassword(request);
            password = (password != null) ? password : "";
            System.out.println(username+" "+password);
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);
            return authentication;

        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            throw e;
        }
    }


    /**
     * 이 함수( attemptAuthentication ) 실행 후 인증이 정상적으로 이루어 지면 successfulAuthentication 함수가 실행 됨
     * JWT 토큰을 만들어 response 에 JWT 토큰을 담아 사용자에게 보냄
     */
    /**
     *  RSA 방식은 아니고 Hash 암호방식
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행 됨 - 인증 완료");
        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = com.auth0.jwt.JWT.create()
                .withSubject(principalDetailis.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetailis.getUser().getId())
                .withClaim("email", principalDetailis.getUser().getEmail())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
    }
}
