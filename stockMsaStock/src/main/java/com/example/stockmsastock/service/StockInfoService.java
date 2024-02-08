package com.example.stockmsastock.service;

import com.example.stockmsastock.domain.stock.Stock;
import com.example.stockmsastock.repository.StockJdbcRepository;
import com.example.stockmsastock.repository.StockJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StockInfoService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StockJpaRepository stockJpaRepository;
    private final StockJdbcRepository stockJdbcRepository;
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private static final String KOSDAQ_URL = "https://m.stock.naver.com/api/stocks/marketValue/KOSDAQ?page={page}&pageSize={pageSize}";
    private static final String KOSDAQ_URL2 = "https://m.stock.naver.com/api/stocks/marketValue/KOSDAQ";
    private static final String KOSPI_URL = "https://m.stock.naver.com/api/stocks/marketValue/KOSPI?page={page}&pageSize={pageSize}";
    private static final String KOSPI_URL2 = "https://m.stock.naver.com/api/stocks/marketValue/KOSPI";
    private final int maxPageSize = 100;
    private final WebClient webClient = WebClient.builder().baseUrl("https://m.stock.naver.com/api").build();


    @Value("${krx.api.Encoding.key}")
    private String serviceKey;
    public List<Stock> getStockInfo() throws JsonProcessingException, MalformedURLException {
        long startTime = System.currentTimeMillis();
        //전날의 정보는 모두 지운다.
        stockJpaRepository.deleteAllInBatch();

        List<Stock> kosdaqStocks=getStocksByCategory(KOSDAQ_URL);
        stockJpaRepository.saveAll(kosdaqStocks);
        List<Stock> kospiStocks = getStocksByCategory(KOSPI_URL);
        stockJpaRepository.saveAll(kospiStocks);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("실행 시간: " + duration + "밀리초");
        return kosdaqStocks;
    }

    public List<Stock> getStockInfo2() throws JsonProcessingException, MalformedURLException {
        long startTime = System.currentTimeMillis();
        //전날의 정보는 모두 지운다.
        stockJpaRepository.deleteAllInBatch();

        CompletableFuture<List<Stock>> kosdaqFuture = CompletableFuture.supplyAsync(() -> {
            long kosdaqStartTime = System.currentTimeMillis();
            List<Stock> kosdaqStocks = null;
            try {
                kosdaqStocks = fetchStockData(KOSDAQ_URL);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            long kosdaqEndTime = System.currentTimeMillis();
            System.out.println("코스닥 작업 시작 시간: " + kosdaqStartTime + ", 종료 시간: " + kosdaqEndTime);
            return kosdaqStocks;
        });

        CompletableFuture<List<Stock>> kospiFuture = CompletableFuture.supplyAsync(() -> {
            long kospiStartTime = System.currentTimeMillis();
            List<Stock> kospiStocks = null;
            try {
                kospiStocks = fetchStockData(KOSPI_URL);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            long kospiEndTime = System.currentTimeMillis();
            System.out.println("코스피 작업 시작 시간: " + kospiStartTime + ", 종료 시간: " + kospiEndTime);
            return kospiStocks;
        });

        List<Stock> kosdaqStocks = kosdaqFuture.join();
        List<Stock> kospiStocks = kospiFuture.join();
        kosdaqStocks.addAll(kospiStocks);
        System.out.println("addAll");
        stockJdbcRepository.batchInsertStocks(kosdaqStocks);
//        stockJpaRepository.saveAll(kosdaqStocks);
//        stockJpaRepository.saveAll(kospiStocks);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("실행 시간: " + duration + "밀리초");
        return kosdaqStocks;
    }

    public Flux<Stock> getStockInfo3() throws JsonProcessingException, MalformedURLException {
        long startTime = System.currentTimeMillis();
        //전날의 정보는 모두 지운다.
        stockJpaRepository.deleteAllInBatch();

//        Flux<Stock> stockFlux = fetchStockDataPerPage(KOSDAQ_URL2, 2);
//        List<Stock> stocks = stockFlux.collectList().block();
//        System.out.println(stocks);

        Flux<Stock> kosdaqStocks = fetchStockData2(KOSDAQ_URL2);
        Flux<Stock> kospiStocks = fetchStockData2(KOSPI_URL2);
        Flux<Stock> mergedFlux = Flux.merge(
                kosdaqStocks.subscribeOn(Schedulers.parallel()),
                kospiStocks.subscribeOn(Schedulers.parallel())
        );

        mergedFlux.flatMap(stock -> Mono.fromCallable(() -> stockJpaRepository.save(stock)))
                .subscribe();

//        List<Stock> stocksList = allStocks.collectList().block();

        // 한 번에 저장
//        stockJpaRepository.saveAll(stocksList);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("실행 시간: " + duration + "밀리초");
        return kosdaqStocks;
    }

    private List<Stock> getStocksByCategory(String url) throws JsonProcessingException, MalformedURLException {
        List<Stock> stocks = new ArrayList<>();
        int totalCount = getTotalCount(url);
        JsonNode rootNode;
        ResponseEntity<String> response;
        String responseBody;

//        System.out.println(totalCount);
        // 1250개면 13번 가져와야 함
        for(int i=1;i<=totalCount/100+1;i++) {
            response = restTemplate.getForEntity(url, String.class,i,maxPageSize);
            responseBody = response.getBody();
            rootNode = objectMapper.readTree(responseBody);

            if (responseBody != null) {
                JsonNode stocksNode = rootNode.get("stocks");
                for (JsonNode stockNode : stocksNode) {
                    Stock stock = Stock.builder()
                            .stockName(stockNode.get("stockName").asText())
                            .itemCode(stockNode.get("itemCode").asText())
                            .category("KOSDAQ")
                            .build();
//                    System.out.println(stock);
                    stocks.add(stock);
                }
            }
        }
        return stocks;
    }

    public List<Stock> fetchStockData(String url) throws JsonProcessingException {
        int totalCount = getTotalCount(url);
        System.out.println(totalCount);

        List<Stock> stocks = new ArrayList<>();
        int totalPages = (totalCount / maxPageSize) + 1;
        ExecutorService executor = Executors.newFixedThreadPool(totalPages); // 쓰레드 풀 생성

        try {
            for (int i = 1; i <= totalPages; i++) {
                int page = i;
                executor.submit(() -> {
                    ResponseEntity<String> responseTemp = restTemplate.getForEntity(url, String.class, page, maxPageSize);
                    String responseBodyTemp = responseTemp.getBody();
                    if (responseBodyTemp != null) {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode rootNodeTemp = objectMapper.readTree(responseBodyTemp);
                            JsonNode stocksNode = rootNodeTemp.get("stocks");
                            for (JsonNode stockNode : stocksNode) {
                                Stock stock = Stock.builder()
                                        .stockName(stockNode.get("stockName").asText())
                                        .itemCode(stockNode.get("itemCode").asText())
                                        .category("KOSDAQ")
                                        .build();
                                stocks.add(stock);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } finally {
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return stocks;
    }


    public Flux<Stock> fetchStockData2(String url) {
        return getTotalCount2(url)
                .flatMapMany(totalCount -> {
                    int totalPages = (totalCount / maxPageSize) + 1;
                    return Flux.range(1, totalPages)
                            .parallel() // 멀티 쓰레드로 작업을 병렬로 실행
                            .runOn(Schedulers.parallel()) // 병렬 스케줄러에서 실행
                            .flatMap(page -> fetchStockDataPerPage(url, page));
                });
    }


    private Mono<Integer> getTotalCount2(String url) {
        return webClient.get()
                .uri(url + "?page=1&pageSize=1")
                .retrieve()
                .bodyToMono(String.class)
                .map(responseBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(responseBody);
                        return rootNode.get("totalCount").asInt();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to parse response", e);
                    }
                });
    }

    private Flux<Stock> fetchStockDataPerPage(String url, int page) {
        long startTime = System.currentTimeMillis(); // 시작 시간 측정
        return webClient.get()
                .uri(url + "?page={page}&pageSize={pageSize}", page, maxPageSize)
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(responseBody -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = null;
                    try {
                        rootNode = objectMapper.readTree(responseBody);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    JsonNode stocksNode = rootNode.get("stocks");
                    Flux<Stock> stockFlux = Flux.fromIterable(stocksNode)
                            .filter(stockNode -> stockNode.hasNonNull("stockName") && stockNode.hasNonNull("itemCode"))
                            .map(stockNode -> Stock.builder()
                                    .stockName(stockNode.get("stockName").asText())
                                    .itemCode(stockNode.get("itemCode").asText())
                                    .category("KOSDAQ")
                                    .build());
                    return stockFlux;
                }).doOnSubscribe(subscription -> {
                    System.out.println("Fetching stock data started at: " + startTime);
                })
                .doOnComplete(() -> {
                    long endTime = System.currentTimeMillis(); // 끝 시간 측정
                    System.out.println("Fetching stock data completed at: " + endTime);
                    System.out.println("cost time: "+ (endTime-startTime));
                });

    }

    private int getTotalCount(String url) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class,1,1);
        String responseBody = response.getBody();
//        System.out.println(responseBody);
        JsonNode rootNode = objectMapper.readTree(responseBody);
        int totalCount = rootNode.get("totalCount").asInt();
        return totalCount;
    }
}