package com.example.stockmsauser.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.stockmsauser.config.auth.PrincipalDetails;
import com.example.stockmsauser.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {

    private static final String ROLE_CLAIM_KEY = "role";
    private static final String ID_CLAIM_KEY = "id";

    public Long getUserId(){
        return findUserFromAuth().getId();
    }

    public static Date getExpirationDateFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token);
        return decodedJWT.getExpiresAt();
    }

    public static User findUserFromAuth() {
        Authentication authentication = getAuthentication();
        if (!isValidAuthentication(authentication)) return null;
        if (!isInstanceOfPrincipalDetails(authentication)) {
            return null;
        }
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        return user;
    }

    public static Long getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token);
        return decodedJWT.getClaim(ID_CLAIM_KEY).asLong();
    }
    public static String getUserRoleFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token);
        return decodedJWT.getClaim(ROLE_CLAIM_KEY).asString();
    }

    public static String getTokenFromHeader(String header){
        if (!isValidToken(header)) return null;
        String token = header.replace(JwtProperties.TOKEN_PREFIX, "");
        return token;
    }

    private static boolean isValidToken(String header) {
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }

    private static boolean isInstanceOfPrincipalDetails(Authentication authentication) {
        return authentication.getPrincipal() instanceof PrincipalDetails;
    }

    private static boolean isValidAuthentication(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
