package com.example.stockmsauser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class TestRedisConfig {
    private RedisServer redisServer;

    public TestRedisConfig(@Value("${spring.redis.port}") int redisPort) {
        redisServer = new RedisServer(6380);
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("레디스 시작");
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("레디스 종료");
        redisServer.stop();
    }
}
