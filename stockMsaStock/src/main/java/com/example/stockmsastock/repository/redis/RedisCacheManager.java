package com.example.stockmsastock.repository.redis;

import com.example.stockmsastock.domain.stockPrice.StockPrice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisCacheManager {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    public StockPrice getStockPriceFromCache(String itemCode) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get(itemCode);
        if(json!=null) return mapJsonToStockPrice(json);
        return null;
    }

    public void saveStockPriceToCache(String itemCode, StockPrice stockPrice) throws JsonProcessingException {
        String json = mapStockPriceToJson(stockPrice);
        redisTemplate.opsForValue().set(itemCode, json);
    }

    @Scheduled(cron = "0 */5 * * * *") // 매 5분마다 실행
    public void clearCache() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    private String mapStockPriceToJson(StockPrice stockPrice) throws JsonProcessingException {
        return objectMapper.writeValueAsString(stockPrice);
    }

    // JSON 문자열을 StockPrice 객체로 역직렬화하는 메서드
    private StockPrice mapJsonToStockPrice(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, StockPrice.class);
    }
}
