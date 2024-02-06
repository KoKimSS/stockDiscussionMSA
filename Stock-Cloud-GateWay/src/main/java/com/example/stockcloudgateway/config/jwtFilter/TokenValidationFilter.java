package com.example.stockcloudgateway.config.jwtFilter;

import com.example.stockcloudgateway.config.jwt.JwtProperties;
import com.example.stockcloudgateway.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

import static com.example.stockcloudgateway.config.jwt.JwtProperties.*;
import static com.example.stockcloudgateway.config.jwt.JwtUtil.*;

@Configuration
@RequiredArgsConstructor
public class TokenValidationFilter {

    private static final String BLACKLIST_KEY = "jwt-blacklist:";
    private final RedisTemplate<String, String> redisTemplate;
    @Bean
    public GlobalFilter tokenFilter() {
        return (exchange, chain) -> {
            // 토큰 검증 로직 수행
            String jwtToken = getTokenFromHeader(exchange.getRequest().getHeaders().getFirst(HEADER_STRING));
            System.out.println(jwtToken);
            if(jwtToken==null){
                return chain.filter(exchange);
            }

            if(!isTokenExpired(jwtToken)&&!isTokenBlacklisted(jwtToken)) {
                Long userId = getUserIdFromToken(jwtToken);
                String userRole = getUserRoleFromToken(jwtToken);
//                exchange.getRequest().mutate().header("id", String.valueOf(userId)).build();
//                exchange.getRequest().mutate().header("role", userRole).build();
                System.out.println(userId+" "+userRole);
            }
            return chain.filter(exchange);
        };
    }

    private boolean isTokenBlacklisted(String token) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(BLACKLIST_KEY, token);
    }
}

