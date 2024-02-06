package com.example.stockmsaactivity.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String ROLE_CLAIM_KEY = "role";
    private static final String ID_CLAIM_KEY = "id";

    public static boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);

        if (expirationDate == null) {
            return false; // 만료 날짜가 설정되지 않은 토큰은 만료되지 않은 것으로 처리
        }
        // 현재 시간과 비교하여 토큰이 만료되었는지 확인
        return expirationDate.before(new Date());
    }

    private static Date getExpirationDateFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token);
        return decodedJWT.getExpiresAt();
    }

    public static Long getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token);
        System.out.println(decodedJWT);
        System.out.println(decodedJWT.getClaim(ID_CLAIM_KEY).asLong());
        return decodedJWT.getClaim(ID_CLAIM_KEY).asLong();
    }

    public static String getUserRoleFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token);
        return decodedJWT.getClaim(ROLE_CLAIM_KEY).asString();
    }

    public static String getTokenFromHeader(String header) {
        if (!isValidToken(header)) return null;
        String token = header.replace(JwtProperties.TOKEN_PREFIX, "");
        return token;
    }

    public static boolean isValidToken(String header) {
        return header != null && header.startsWith(JwtProperties.TOKEN_PREFIX);
    }
}
