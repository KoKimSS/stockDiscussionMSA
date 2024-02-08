package com.example.stockmsastock.service;

import com.example.stockmsastock.domain.stock.StockPrice;
import com.example.stockmsastock.repository.redis.RedisCacheManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StockPriceInfoService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final RedisCacheManager redisCacheManager;
    private static final String STOCK_PRICE_URL = "https://polling.finance.naver.com/api/realtime/domestic/stock/";


    public StockPrice getStockPrice(String itemCode) throws JsonProcessingException, MalformedURLException {
        StockPrice stockPriceFromCache = redisCacheManager.getStockPriceFromCache(itemCode);
        System.out.println(stockPriceFromCache);
        if(stockPriceFromCache!=null) return stockPriceFromCache;
        ResponseEntity<String> response = restTemplate.getForEntity(STOCK_PRICE_URL+itemCode, String.class);
        String json = response.getBody();
        System.out.println(json);
        StockPrice stockPrice = mappedByJson(json);
        redisCacheManager.saveStockPriceToCache(itemCode,stockPrice);
        return stockPrice;
    }



    public StockPrice mappedByJson(String json){
        StockPrice stockPrice = null;
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode datasNode = rootNode.get("datas");
            if (datasNode.isArray()) {
                for (JsonNode dataNode : datasNode) {
                    String itemCode = dataNode.get("itemCode").asText();
                    String stockName = dataNode.get("stockName").asText();
                    String closePrice = dataNode.get("closePrice").asText().replace(",", "");
                    String compareToPreviousClosePrice = dataNode.get("compareToPreviousClosePrice").asText().replace(",", "");
                    String fluctuationsRatio = dataNode.get("fluctuationsRatio").asText();
                    String openPrice = dataNode.get("openPrice").asText().replace(",", "");
                    String highPrice = dataNode.get("highPrice").asText().replace(",", "");
                    String lowPrice = dataNode.get("lowPrice").asText().replace(",", "");
                    String accumulatedTradingVolume = dataNode.get("accumulatedTradingVolume").asText().replace(",", "");
                    String accumulatedTradingValue = dataNode.get("accumulatedTradingValue").asText().replaceAll("[^0-9]", "");

                    stockPrice = StockPrice.builder()
                            .itemCode(itemCode)
                            .stockName(stockName)
                            .closePrice(closePrice)
                            .compareToPreviousClosePrice(compareToPreviousClosePrice)
                            .fluctuationsRatio(fluctuationsRatio)
                            .openPrice(openPrice)
                            .highPrice(highPrice)
                            .lowPrice(lowPrice)
                            .accumulatedTradingVolume(accumulatedTradingVolume)
                            .accumulatedTradingValue(accumulatedTradingValue)
                            .build();


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockPrice;
    }
}
