package com.example.stockmsastock.web.controller;

import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.repository.StockRepository;
import com.example.stockmsastock.repository.StockRepositoryCustom;
import com.example.stockmsastock.service.StockInfoService;
import com.example.stockmsastock.web.dto.request.GetStockBySortRequestDto;
import com.example.stockmsastock.web.dto.request.GetStockInfoRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetStockController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StockInfoService stockInfoService;
    private final StockRepository stockRepository;



    @PostMapping("/stocks")
    public ResponseEntity<Page<Stock>> getAllStocksOrderedBy(@RequestBody GetStockBySortRequestDto stockRequest, Pageable pageable) {
        Page<Stock> stocks = stockRepository.findAllOrderedBy(
                stockRequest.getSortBy(),
                stockRequest.getSortOrder(),
                pageable
        );
        return ResponseEntity.ok().body(stocks);
    }

    @GetMapping("/get-stock-infos1")
    ResponseEntity getStockInfos1() throws JsonProcessingException, MalformedURLException {
        List<Stock> stocks = stockInfoService.getStockInfo();
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @GetMapping("/get-stock-infos2")
    ResponseEntity getStockInfos2() throws JsonProcessingException, MalformedURLException {
        List<Stock> stocks = stockInfoService.getStockInfo2();
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }


    @GetMapping("/get-stock-infos3")
    ResponseEntity getStockInfos3() throws JsonProcessingException, MalformedURLException {
        Flux<Stock> stocks = stockInfoService.getStockInfo3();
        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String url = "https://m.stock.naver.com/api/stocks/marketValue/KOSDAQ?page=1&pageSize=100";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        List<Stock> stocks = new ArrayList<>();

        // 여기서 response.getBody()를 통해 JSON을 파싱하고 필요한 값을 추출하는 작업을 수행합니다.
        // 이전에 제공한 JSON 파싱 방법과 유사한 방법을 사용할 수 있습니다.
        String responseBody = response.getBody();
        if (responseBody != null) {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode stocksNode = rootNode.get("stocks");
            int totalCount = rootNode.get("totalCount").asInt();

            for (JsonNode stockNode : stocksNode) {
                Stock stock = Stock.builder()
                        .stockName(stockNode.get("stockName").asText())
                        .itemCode(stockNode.get("itemCode").asText())
                        .category("KOSDAQ")
                        .build();
                System.out.println(stock);
                stocks.add(stock);
            }
        }

        return stocks;
    }
}
